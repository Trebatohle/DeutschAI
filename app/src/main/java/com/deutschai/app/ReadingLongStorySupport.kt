package com.deutschai.app

import android.util.Base64
import java.io.ByteArrayInputStream
import java.util.zip.GZIPInputStream

internal fun decodeReadingBody(encoded: String): String {
    val compact = encoded.replace("\n", "")
    val compressed = Base64.decode(compact, Base64.DEFAULT)
    return GZIPInputStream(ByteArrayInputStream(compressed)).bufferedReader(Charsets.UTF_8).use { it.readText() }
}

internal fun buildAdvancedReadingBody(seed: String, extension: List<String>): String {
    val seedText = seed.trim()
    val paragraphs = extension.map { it.trim() }.filter { it.isNotEmpty() }
    require(seedText.isNotEmpty()) { "Reading story seed must not be empty." }
    require(paragraphs.isNotEmpty()) { "Reading story continuation must not be empty." }
    require(paragraphs.size == paragraphs.toSet().size) {
        "Reading story continuation contains duplicate paragraphs."
    }

    val body = (listOf(seedText) + paragraphs).joinToString("\n\n")
    val wordCount = body.split(Regex("\\s+")).count { it.isNotBlank() }
    require(wordCount > 1000) {
        "Reading story must exceed 1,000 words, but contains $wordCount."
    }
    return body
}
