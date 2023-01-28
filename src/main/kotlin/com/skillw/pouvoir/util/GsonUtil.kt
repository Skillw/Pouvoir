package com.skillw.pouvoir.util

import com.google.gson.GsonBuilder
import com.skillw.pouvoir.util.Gson.gson
import taboolib.common.env.RuntimeDependencies
import taboolib.common.env.RuntimeDependency

/**
 * Gson工具类
 *
 * @constructor Create empty Gson utils
 */
@RuntimeDependencies(
    RuntimeDependency("com.google.code.gson:gson:2.9.0", test = "com.google.gson.Gson")
)
object Gson {
    val gson by lazy {
        GsonBuilder().disableJdkUnsafe().serializeNulls().create()
    }
}


inline fun <reified T> T.encodeJson(): String {
    return gson.toJson(this)
}


inline fun <reified T> String.decodeFromString(): T? {
    return gson.fromJson(this, T::class.java)
}