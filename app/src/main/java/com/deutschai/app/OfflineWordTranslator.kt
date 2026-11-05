package com.deutschai.app

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import java.util.concurrent.atomic.AtomicBoolean

/**
 * German-to-Czech translation for words missing from the bundled dictionaries.
 * The model is downloaded once and then translates locally on-device.
 */
internal object OfflineWordTranslator {
    private const val TRANSLATION_TIMEOUT_MILLIS = 30_000L

    private val lock = Any()
    private var translator: Translator? = null
    private var downloading = false
    private var modelReady = false
    private val waiting = mutableListOf<(Boolean) -> Unit>()
    private val mainHandler = Handler(Looper.getMainLooper())

    private fun client(context: Context): Translator = synchronized(lock) {
        translator ?: Translation.getClient(
            TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.GERMAN)
                .setTargetLanguage(TranslateLanguage.CZECH)
                .build()
        ).also { translator = it }
    }

    private fun deliver(callback: (Boolean) -> Unit, ready: Boolean) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runCatching { callback(ready) }
        } else {
            mainHandler.post { runCatching { callback(ready) } }
        }
    }

    fun prepare(context: Context, callback: (Boolean) -> Unit) {
        val activeClient = runCatching { client(context.applicationContext) }.getOrElse {
            deliver(callback, false)
            return
        }
        var alreadyReady = false
        var startDownload = false
        synchronized(lock) {
            if (modelReady) {
                alreadyReady = true
            } else {
                waiting.add(callback)
                if (!downloading) {
                    downloading = true
                    startDownload = true
                }
            }
        }

        if (alreadyReady) {
            deliver(callback, true)
            return
        }
        if (!startDownload) return

        try {
            // Do not require Wi-Fi: the learner may prepare the model over any
            // available connection. After the one-time download, translation is offline.
            activeClient.downloadModelIfNeeded(
                DownloadConditions.Builder().build()
            ).addOnSuccessListener {
                finishPreparation(true)
            }.addOnFailureListener {
                finishPreparation(false)
            }.addOnCanceledListener {
                finishPreparation(false)
            }
        } catch (_: Throwable) {
            finishPreparation(false)
        }
    }

    private fun finishPreparation(success: Boolean) {
        val callbacks: List<(Boolean) -> Unit>
        synchronized(lock) {
            modelReady = success
            downloading = false
            callbacks = waiting.toList()
            waiting.clear()
        }
        callbacks.forEach { callback -> deliver(callback, success) }
    }

    fun translate(context: Context, text: String, callback: (String?) -> Unit) {
        val completed = AtomicBoolean(false)
        val timeout = Runnable {
            if (completed.compareAndSet(false, true)) callback(null)
        }
        mainHandler.postDelayed(timeout, TRANSLATION_TIMEOUT_MILLIS)

        fun finish(result: String?) {
            if (!completed.compareAndSet(false, true)) return
            mainHandler.removeCallbacks(timeout)
            if (Looper.myLooper() == Looper.getMainLooper()) {
                callback(result)
            } else {
                mainHandler.post { callback(result) }
            }
        }

        try {
            prepare(context.applicationContext) { ready ->
                if (!ready) {
                    finish(null)
                } else {
                    try {
                        client(context.applicationContext).translate(text)
                            .addOnSuccessListener { finish(it) }
                            .addOnFailureListener { finish(null) }
                            .addOnCanceledListener { finish(null) }
                    } catch (_: Throwable) {
                        finish(null)
                    }
                }
            }
        } catch (_: Throwable) {
            finish(null)
        }
    }
}
