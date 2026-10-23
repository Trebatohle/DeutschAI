package com.deutschai.app

import android.content.Context
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions

/**
 * German-to-Czech translation for vocabulary missing from the bundled dictionary.
 * The translation model downloads once over Wi-Fi and then translates locally on-device.
 */
internal object OfflineWordTranslator {
    private val lock = Any()
    private var translator: Translator? = null
    private var downloading = false
    private var modelReady = false
    private val waiting = mutableListOf<(Boolean) -> Unit>()

    private fun client(context: Context): Translator = synchronized(lock) {
        translator ?: Translation.getClient(
            TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.GERMAN)
                .setTargetLanguage(TranslateLanguage.CZECH)
                .build()
        ).also { translator = it }
    }

    fun prepare(context: Context, callback: (Boolean) -> Unit) {
        val activeClient = client(context.applicationContext)
        synchronized(lock) {
            if (modelReady) {
                callback(true)
                return
            }
            waiting.add(callback)
            if (downloading) return
            downloading = true
        }

        activeClient.downloadModelIfNeeded(
            DownloadConditions.Builder().requireWifi().build()
        ).addOnSuccessListener {
            finishPreparation(true)
        }.addOnFailureListener {
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
        callbacks.forEach { callback -> runCatching { callback(success) } }
    }

    fun translate(context: Context, text: String, callback: (String?) -> Unit) {
        prepare(context.applicationContext) { ready ->
            if (!ready) {
                callback(null)
            } else {
                client(context.applicationContext).translate(text)
                    .addOnSuccessListener { callback(it) }
                    .addOnFailureListener { callback(null) }
            }
        }
    }
}
