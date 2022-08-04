package com.skillw.pouvoir.util

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


object GsonUtils {

    @JvmStatic
    inline fun <reified T> T.encodeJson(): String {
        return Json.encodeToString(this)
    }


    @JvmStatic
    inline fun <reified T> String.decodeFromString(): T? {
        return Json.decodeFromString(this)
    }
}