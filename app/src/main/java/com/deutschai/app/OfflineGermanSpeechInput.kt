package com.deutschai.app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat

/**
 * Uses Android's on-device speech recognizer only. It deliberately does not fall
 * back to the network recognizer when local German recognition is unavailable.
 */
@Composable
internal fun OfflineGermanSpeechInput(
    onRecognized: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val onDeviceAvailable = remember(context) {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            SpeechRecognizer.isOnDeviceRecognitionAvailable(context)
    }
    val recognizerRef = remember { arrayOfNulls<SpeechRecognizer>(1) }
    var transcript by remember { mutableStateOf("") }
    var status by remember {
        mutableStateOf(
            if (onDeviceAvailable) {
                "Odpověď se rozpoznává přímo v telefonu; zvuk se neodesílá na server."
            } else {
                "Tento telefon nemá dostupné lokální rozpoznávání němčiny. Můžeš dál procvičovat pomocí nabízených odpovědí."
            }
        )
    }
    var isListening by remember { mutableStateOf(false) }
    var startRequested by remember { mutableStateOf(false) }
    var permissionGranted by remember {
        mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        permissionGranted = granted
        if (granted) {
            startRequested = true
        } else {
            status = "Bez povolení mikrofonu nemohu rozpoznat odpověď. Ostatní části aplikace fungují dál offline."
        }
    }

    fun beginListening() {
        if (!onDeviceAvailable) {
            status = "Lokální rozpoznávání řeči není na tomto zařízení dostupné."
            return
        }
        val recognizer = runCatching {
            SpeechRecognizer.createOnDeviceSpeechRecognizer(context.applicationContext)
        }.getOrNull()
        if (recognizer == null) {
            status = "Nepodařilo se spustit lokální rozpoznávání řeči."
            return
        }

        recognizerRef[0]?.destroy()
        recognizerRef[0] = recognizer
        recognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                isListening = true
                status = "Poslouchám… řekni německou odpověď."
            }

            override fun onBeginningOfSpeech() {
                isListening = true
            }

            override fun onRmsChanged(rmsdB: Float) = Unit
            override fun onBufferReceived(buffer: ByteArray?) = Unit

            override fun onEndOfSpeech() {
                isListening = false
                status = "Vyhodnocuji vyslovenou větu…"
            }

            override fun onError(error: Int) {
                isListening = false
                status = when (error) {
                    SpeechRecognizer.ERROR_NO_MATCH,
                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "Nerozuměla jsem. Zkus to znovu a řekni kratší větu."
                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Telefon nepovolil přístup k mikrofonu."
                    SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Rozpoznávání je právě zaneprázdněné. Zkus to za chvíli."
                    else -> "Rozpoznání se nepodařilo. Zkus to znovu nebo vyber odpověď ručně."
                }
            }

            override fun onResults(results: Bundle?) {
                isListening = false
                val recognized = results
                    ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    ?.firstOrNull()
                    .orEmpty()
                if (recognized.isNotBlank()) {
                    transcript = recognized
                    status = "Takhle jsem tvou odpověď rozpoznala:"
                    onRecognized(recognized)
                } else {
                    status = "Nepodařilo se zachytit celou odpověď. Zkus to znovu."
                }
            }

            override fun onPartialResults(partialResults: Bundle?) = Unit
            override fun onEvent(eventType: Int, params: Bundle?) = Unit
        })

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "de-DE")
            putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }
        transcript = ""
        isListening = true
        status = "Spouštím mikrofon…"
        runCatching { recognizer.startListening(intent) }.onFailure {
            isListening = false
            status = "Mikrofon se nepodařilo spustit. Zkus to znovu."
        }
    }

    LaunchedEffect(startRequested) {
        if (startRequested) {
            startRequested = false
            if (permissionGranted) beginListening()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            runCatching { recognizerRef[0]?.cancel() }
            runCatching { recognizerRef[0]?.destroy() }
            recognizerRef[0] = null
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        Button(
            onClick = {
                if (!onDeviceAvailable) {
                    status = "Lokální rozpoznávání řeči není na tomto zařízení dostupné; aplikace nepoužije online náhradu."
                } else if (!permissionGranted) {
                    permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                } else {
                    startRequested = true
                }
            },
            enabled = onDeviceAvailable && !isListening,
            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = Forest),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.Mic, contentDescription = null)
            Text(if (isListening) "Poslouchám…" else "Zkusit odpovědět hlasem", modifier = Modifier.padding(start = 7.dp), fontWeight = FontWeight.Bold)
        }
        Text(status, color = Muted, fontSize = 12.sp, lineHeight = 18.sp)
        if (transcript.isNotBlank()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("ROZPOZNANÁ ODPOVĚĎ", color = Forest, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Text(transcript, color = Ink, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, lineHeight = 22.sp)
                }
            }
        }
    }
}
