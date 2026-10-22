package com.deutschai.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.speech.tts.Voice
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale
import java.util.concurrent.atomic.AtomicLong

internal enum class GermanVoiceStatus { LOADING, READY, UNAVAILABLE, ERROR }

/**
 * Speaks only with an installed German (Germany, de-DE) voice.
 * It deliberately refuses language fallbacks such as Czech or English.
 */
internal class GermanSpeechPlayer(private val context: Context) {
    var status by mutableStateOf(GermanVoiceStatus.LOADING)
        private set
    var isSpeaking by mutableStateOf(false)
        private set
    var selectedVoiceName by mutableStateOf<String?>(null)
        private set

    private var engine: TextToSpeech? = null
    private val mainHandler = Handler(Looper.getMainLooper())
    private val utteranceCounter = AtomicLong()
    private val completionCallbacks = mutableMapOf<String, () -> Unit>()
    private var initialized = false

    fun initialize() {
        if (initialized) return
        initialized = true
        status = GermanVoiceStatus.LOADING
        try {
            engine = TextToSpeech(context.applicationContext) { result ->
                mainHandler.post {
                    val tts = engine
                    if (result != TextToSpeech.SUCCESS || tts == null) {
                        status = GermanVoiceStatus.ERROR
                        return@post
                    }

                    val germanVoices = tts.voices.orEmpty()
                        .filter {
                            it.locale.language.equals("de", ignoreCase = true) &&
                                it.locale.country.equals("DE", ignoreCase = true)
                        }
                        // Prefer a voice that does not require a network connection.
                        .sortedWith(
                            compareBy<Voice> { it.isNetworkConnectionRequired }
                                .thenByDescending { it.quality }
                        )
                    val chosen = germanVoices.firstOrNull()
                    if (chosen == null) {
                        status = GermanVoiceStatus.UNAVAILABLE
                        return@post
                    }

                    val languageResult = tts.setLanguage(Locale.GERMANY)
                    if (languageResult < TextToSpeech.LANG_AVAILABLE ||
                        tts.setVoice(chosen) != TextToSpeech.SUCCESS
                    ) {
                        status = GermanVoiceStatus.UNAVAILABLE
                        return@post
                    }

                    val actualLocale = tts.voice?.locale
                    if (actualLocale?.language.equals("de", ignoreCase = true) &&
                        actualLocale?.country.equals("DE", ignoreCase = true)
                    ) {
                        selectedVoiceName = chosen.name
                        status = GermanVoiceStatus.READY
                        tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                            override fun onStart(utteranceId: String?) {
                                mainHandler.post { isSpeaking = true }
                            }

                            override fun onDone(utteranceId: String?) {
                                mainHandler.post {
                                    isSpeaking = false
                                    if (utteranceId != null) {
                                        completionCallbacks.remove(utteranceId)?.invoke()
                                    }
                                }
                            }

                            override fun onError(utteranceId: String?) {
                                mainHandler.post {
                                    isSpeaking = false
                                    if (utteranceId != null) completionCallbacks.remove(utteranceId)
                                }
                            }

                            override fun onError(utteranceId: String?, errorCode: Int) {
                                onError(utteranceId)
                            }
                        })
                    } else {
                        status = GermanVoiceStatus.UNAVAILABLE
                    }
                }
            }
        } catch (_: Exception) {
            status = GermanVoiceStatus.ERROR
        }
    }

    fun speak(text: String, slow: Boolean = false, onCompleted: (() -> Unit)? = null): Boolean {
        val tts = engine ?: return false
        if (status != GermanVoiceStatus.READY || text.isBlank()) return false

        tts.stop()
        completionCallbacks.clear()
        isSpeaking = false

        val utteranceId = "deutschai_${utteranceCounter.incrementAndGet()}"
        if (onCompleted != null) completionCallbacks[utteranceId] = onCompleted
        tts.setSpeechRate(if (slow) 0.62f else 1.0f)
        val result = tts.speak(text, TextToSpeech.QUEUE_FLUSH, Bundle(), utteranceId)
        if (result != TextToSpeech.SUCCESS) {
            completionCallbacks.remove(utteranceId)
            isSpeaking = false
            return false
        }
        return true
    }

    fun stop() {
        engine?.stop()
        completionCallbacks.clear()
        isSpeaking = false
    }

    fun retry() {
        shutdown()
        initialize()
    }

    fun shutdown() {
        stop()
        engine?.shutdown()
        engine = null
        initialized = false
    }
}

@Composable
internal fun rememberGermanSpeechPlayer(): GermanSpeechPlayer {
    val context = LocalContext.current
    val player = remember(context) { GermanSpeechPlayer(context.applicationContext) }
    DisposableEffect(player) {
        player.initialize()
        onDispose { player.shutdown() }
    }
    return player
}

@Composable
internal fun GermanAudioControls(
    player: GermanSpeechPlayer,
    text: String,
    modifier: Modifier = Modifier,
    showSlow: Boolean = true,
    onPlaybackCompleted: (() -> Unit)? = null
) {
    val context = LocalContext.current
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            onClick = { player.speak(text, slow = false, onCompleted = onPlaybackCompleted) },
            enabled = player.status == GermanVoiceStatus.READY,
            modifier = Modifier.weight(1f).heightIn(min = 48.dp),
            contentPadding = PaddingValues(horizontal = 7.dp, vertical = 10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Forest, contentColor = androidx.compose.ui.graphics.Color.White),
            shape = RoundedCornerShape(12.dp)
        ) {
            androidx.compose.material3.Icon(Icons.Default.VolumeUp, contentDescription = null, modifier = Modifier.size(18.dp))
            Text(
                "Poslechnout",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                maxLines = 1,
                softWrap = false,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
        if (showSlow) {
            Button(
                onClick = { player.speak(text, slow = true, onCompleted = onPlaybackCompleted) },
                enabled = player.status == GermanVoiceStatus.READY,
                modifier = Modifier.weight(1f).heightIn(min = 48.dp),
                contentPadding = PaddingValues(horizontal = 7.dp, vertical = 10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = Forest),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Pomalu", fontWeight = FontWeight.Bold, fontSize = 12.sp, maxLines = 1, softWrap = false)
            }
        }
    }

    when (player.status) {
        GermanVoiceStatus.LOADING -> Text(
            "Připravuji německý hlas (de-DE)…",
            color = Muted, fontSize = 12.sp, lineHeight = 18.sp
        )
        GermanVoiceStatus.UNAVAILABLE -> {
            Text(
                "V telefonu není dostupný hlas němčiny pro Německo (de-DE). Jiným jazykem větu nepřehraji.",
                color = androidx.compose.ui.graphics.Color(0xFF9B2C20), fontSize = 12.sp, lineHeight = 18.sp
            )
            Button(
                onClick = {
                    try {
                        context.startActivity(Intent(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA))
                    } catch (_: Exception) {
                        Toast.makeText(context, "Otevři nastavení Převod textu na řeč a nainstaluj německý hlas (Německo).", Toast.LENGTH_LONG).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = Forest),
                shape = RoundedCornerShape(12.dp)
            ) { Text("Nainstalovat německý hlas", fontWeight = FontWeight.Bold) }
            Button(
                onClick = { player.retry() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = androidx.compose.ui.graphics.Color(0xFFEFEDE7), contentColor = Forest),
                shape = RoundedCornerShape(12.dp)
            ) { Text("Znovu ověřit dostupný hlas", fontWeight = FontWeight.Bold) }
        }
        GermanVoiceStatus.ERROR -> Text(
            "Nepodařilo se spustit hlasový engine. Zkontroluj v nastavení telefonu službu Převod textu na řeč.",
            color = androidx.compose.ui.graphics.Color(0xFF9B2C20), fontSize = 12.sp, lineHeight = 18.sp
        )
        GermanVoiceStatus.READY -> Unit
    }
}
