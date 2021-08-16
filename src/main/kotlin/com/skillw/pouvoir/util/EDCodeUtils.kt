package com.skillw.pouvoir.util

import java.io.UnsupportedEncodingException
import java.nio.charset.StandardCharsets.UTF_8
import java.util.*

object EDCodeUtils {
    val ENCODER: Base64.Encoder = Base64.getEncoder()
    val DECODER: Base64.Decoder = Base64.getDecoder()

    /**
     * 给字符串加密
     *
     * @param text
     * @return
     */
    @JvmStatic
    fun base64Encode(text: String): String {
        var textByte = ByteArray(0)
        try {
            textByte = text.toByteArray(charset("UTF-8"))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return ENCODER.encodeToString(textByte)
    }

    @JvmStatic
    fun base64Decode(encodedText: String?): String? {
        var text: String? = null
        try {
            text = String(DECODER.decode(encodedText), UTF_8)
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return text
    }
}