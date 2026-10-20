package com.deutschai.app

import android.util.Base64
import java.io.ByteArrayInputStream
import java.util.zip.GZIPInputStream

internal fun decodeReadingBody(encoded: String): String {
    val compact = encoded.replace("\n", "")
    val compressed = Base64.decode(compact, Base64.DEFAULT)
    return GZIPInputStream(ByteArrayInputStream(compressed)).bufferedReader(Charsets.UTF_8).use { it.readText() }
}
