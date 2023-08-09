package com.skillw.pouvoir.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder

/**
 * Gson工具类
 *
 * @constructor Create empty Gson utils
 */

val gson: Gson by lazy {
    GsonBuilder().serializeNulls().create()
}

inline fun <reified T> T.encodeJson(): String {
    return gson.toJson(this)
}


inline fun <reified T> String.decodeFromString(): T? {
    return gson.fromJson(this, T::class.java)
}
