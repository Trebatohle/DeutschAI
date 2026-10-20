package com.deutschai.app

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.google.ai.edge.litertlm.Backend
import com.google.ai.edge.litertlm.Conversation
import com.google.ai.edge.litertlm.ConversationConfig
import com.google.ai.edge.litertlm.Contents
import com.google.ai.edge.litertlm.Engine
import com.google.ai.edge.litertlm.EngineConfig
import com.google.ai.edge.litertlm.SamplerConfig
import com.google.ai.edge.litertlm.ThinkingConfig
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

/**
 * Runs Beatrice's language model on the phone. The model weights are downloaded once;
 * inference itself never calls a network service.
 *
 * Engine and Conversation are intentionally owned by a single background executor.
 */
internal class OfflineAiTutor(context: Context) {
    companion object {
        const val MODEL_DOWNLOAD_LABEL = "cca 329 MB"

        private const val MODEL_FILE_NAME = "Qwen3-0.6B_dynamic_wi4b32_afp32.litertlm"
        private const val MODEL_URL =
            "https://huggingface.co/litert-community/Qwen3-0.6B/resolve/main/Qwen3-0.6B_dynamic_wi4b32_afp32.litertlm"
        private const val MIN_VALID_MODEL_BYTES = 250_000_000L
        private const val BUFFER_SIZE = 256 * 1024

        fun modelFile(context: Context): File =
            File(File(context.applicationContext.filesDir, "offline_ai"), MODEL_FILE_NAME)

        fun isModelInstalled(context: Context): Boolean {
            val file = modelFile(context)
            return file.isFile && file.length() >= MIN_VALID_MODEL_BYTES
        }

        private val systemInstruction = """
            Jsi Beatrice, přátelská a trpělivá učitelka němčiny pro českého studenta.
            Student začíná na úrovni A1. Odpovídej česky, stručně a povzbudivě.
            Posuzuj německou větu podle jejího významu, slovosledu, pádů, členů a tvarů sloves.
            Pokud je věta chybná, uveď nejprve přirozenou opravenou německou větu.
            Pokud je věta správně, výslovně napiš, že je správně.
            Vysvětli česky nejdůležitější chybu nejvýše ve 2 až 3 krátkých větách.
            Když existuje více přirozených správných variant, uznej je.
            Nevymýšlej chyby a nepředstírej jistotu, pokud význam není jasný.
            Používej formát:
            Oprava: ...
            Vysvětlení: ...
            Neodpovídej dlouhým esejem, nevypisuj skryté uvažování a nepřidávej novou otázku.
        """.trimIndent()
    }

    private val appContext = context.applicationContext
    private val mainHandler = Handler(Looper.getMainLooper())
    private val executor = Executors.newSingleThreadExecutor()
    private var engine: Engine? = null
    private var conversation: Conversation? = null
    @Volatile private var closed = false

    fun downloadModel(
        onProgress: (Float) -> Unit,
        onComplete: (Result<Unit>) -> Unit
    ) {
        if (closed) {
            postToMain { onComplete(Result.failure(IllegalStateException("AI služba už není aktivní."))) }
            return
        }

        executor.execute {
            val target = modelFile(appContext)
            val temp = File(target.parentFile, target.name + ".part")
            var connection: HttpURLConnection? = null
            try {
                target.parentFile?.mkdirs()
                temp.delete()

                connection = (URL(MODEL_URL).openConnection() as HttpURLConnection).apply {
                    connectTimeout = 20_000
                    readTimeout = 60_000
                    instanceFollowRedirects = true
                    setRequestProperty("User-Agent", "DeutschAI-Android")
                }

                val responseCode = connection.responseCode
                if (responseCode !in 200..299) {
                    throw IOException("Server modelu vrátil HTTP $responseCode.")
                }

                val expectedBytes = connection.contentLengthLong
                var downloadedBytes = 0L
                var lastReportedBytes = 0L

                BufferedInputStream(connection.inputStream).use { input ->
                    FileOutputStream(temp).use { output ->
                        val buffer = ByteArray(BUFFER_SIZE)
                        while (true) {
                            val count = input.read(buffer)
                            if (count < 0) break
                            output.write(buffer, 0, count)
                            downloadedBytes += count

                            if (
                                downloadedBytes - lastReportedBytes >= 1_000_000L ||
                                (expectedBytes > 0L && downloadedBytes >= expectedBytes)
                            ) {
                                lastReportedBytes = downloadedBytes
                                val progress = if (expectedBytes > 0L) {
                                    (downloadedBytes.toFloat() / expectedBytes.toFloat()).coerceIn(0f, 1f)
                                } else {
                                    0f
                                }
                                postToMain { onProgress(progress) }
                            }
                        }
                        output.fd.sync()
                    }
                }

                if (expectedBytes > 0L && downloadedBytes != expectedBytes) {
                    throw IOException("Stažení modelu nebylo úplné. Zkus to prosím znovu.")
                }
                if (downloadedBytes < MIN_VALID_MODEL_BYTES) {
                    throw IOException("Stažený soubor je příliš malý a nevypadá jako platný model.")
                }

                if (target.exists() && !target.delete()) {
                    throw IOException("Nepodařilo se nahradit starý soubor modelu.")
                }
                if (!temp.renameTo(target)) {
                    temp.copyTo(target, overwrite = true)
                    temp.delete()
                }

                postToMain {
                    onProgress(1f)
                    onComplete(Result.success(Unit))
                }
            } catch (error: Exception) {
                temp.delete()
                postToMain { onComplete(Result.failure(error)) }
            } finally {
                connection?.disconnect()
            }
        }
    }

    fun initialize(onComplete: (Result<Unit>) -> Unit) {
        if (closed) {
            postToMain { onComplete(Result.failure(IllegalStateException("AI služba už není aktivní."))) }
            return
        }

        executor.execute {
            try {
                val file = modelFile(appContext)
                if (!file.isFile || file.length() < MIN_VALID_MODEL_BYTES) {
                    throw IOException("Nejdřív je potřeba stáhnout offline AI model.")
                }

                if (engine == null || conversation == null) {
                    val createdEngine = Engine(
                        EngineConfig(
                            modelPath = file.absolutePath,
                            backend = Backend.CPU(threadCount = 4),
                            maxNumTokens = 2048,
                            cacheDir = File(appContext.cacheDir, "litertlm").apply { mkdirs() }.absolutePath
                        )
                    )
                    var initialized = false
                    try {
                        createdEngine.initialize()
                        initialized = true
                        val createdConversation = createdEngine.createConversation(
                            ConversationConfig(
                                systemInstruction = Contents.of(systemInstruction),
                                samplerConfig = SamplerConfig(
                                    topK = 10,
                                    topP = 0.9,
                                    temperature = 0.2
                                ),
                                // Qwen3 can otherwise spend the output budget on hidden reasoning.
                                // This tutor needs a short correction, not a long chain of thought.
                                maxOutputToken = 96,
                                thinkingConfig = ThinkingConfig(
                                    enableThinking = false,
                                    thinkingTokenBudget = 0
                                )
                            )
                        )
                        engine = createdEngine
                        conversation = createdConversation
                    } catch (error: Exception) {
                        // LiteRT-LM requires close only after a successful initialize.
                        if (initialized) {
                            runCatching { createdEngine.close() }
                        }
                        throw error
                    }
                }

                postToMain { onComplete(Result.success(Unit)) }
            } catch (error: Exception) {
                postToMain { onComplete(Result.failure(error)) }
            }
        }
    }

    fun reviewAnswer(
        question: String,
        answer: String,
        onComplete: (Result<String>) -> Unit
    ) {
        if (closed) {
            postToMain { onComplete(Result.failure(IllegalStateException("AI služba už není aktivní."))) }
            return
        }

        executor.execute {
            try {
                val activeConversation = conversation
                    ?: throw IllegalStateException("Beatrice se ještě připravuje. Zkus to za chvíli.")
                val prompt = """
                    Aktuální otázka: $question
                    Odpověď studenta: $answer

                    Oprav větu podle pravidel v instrukci. Odpověz stručně.
                """.trimIndent()
                val modelMessage = activeConversation.sendMessage(
                    prompt,
                    maxOutputToken = 96,
                    thinkingConfig = ThinkingConfig(
                        enableThinking = false,
                        thinkingTokenBudget = 0
                    )
                )
                // Some model templates return the final answer in a named channel.
                val response = modelMessage.contents.toString()
                    .ifBlank { modelMessage.channels["final"].orEmpty() }
                    .trim()
                if (response.isBlank()) {
                    val channels = modelMessage.channels.keys.joinToString().ifBlank { "žádné" }
                    throw IOException("Model nevrátil text odpovědi (kanály: $channels). Zkus to prosím znovu.")
                }
                postToMain { onComplete(Result.success(response)) }
            } catch (error: Exception) {
                postToMain { onComplete(Result.failure(error)) }
            }
        }
    }

    fun close() {
        if (closed) return
        closed = true
        executor.execute {
            runCatching { conversation?.close() }
            conversation = null
            runCatching { engine?.close() }
            engine = null
        }
        executor.shutdown()
    }

    private fun postToMain(action: () -> Unit) {
        mainHandler.post { action() }
    }
}
