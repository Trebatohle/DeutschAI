package com.deutschai.app

import android.util.Base64
import java.io.ByteArrayInputStream
import java.util.zip.GZIPInputStream

internal fun decodeReadingBody(encoded: String): String {
    val compact = encoded.replace("\n", "")
    val compressed = Base64.decode(compact, Base64.DEFAULT)
    return GZIPInputStream(ByteArrayInputStream(compressed)).bufferedReader(Charsets.UTF_8).use { it.readText() }
}

internal fun buildAdvancedReadingBody(seed: String, extension: List<String>, offset: Int = 0): String {
    val parts = mutableListOf(seed.trim())
    var words = parts[0].split(Regex("\\s+")).size
    var index = offset
    while (words < 1000) {
        val paragraph = extension[index % extension.size]
        parts += paragraph
        words += paragraph.split(Regex("\\s+")).size
        index++
    }
    return parts.joinToString("\n\n")
}
