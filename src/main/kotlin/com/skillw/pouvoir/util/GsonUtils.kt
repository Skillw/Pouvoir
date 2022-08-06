package com.skillw.pouvoir.util

import com.google.gson.Gson
import taboolib.common.env.RuntimeDependencies
import taboolib.common.env.RuntimeDependency

@RuntimeDependencies(
    RuntimeDependency("com.google.code.gson:gson:2.9.0", test = "com.google.gson.Gson")
)
object GsonUtils {

    @JvmStatic
    inline fun <reified T> T.encodeJson(): String {
        return Gson().toJson(this)
    }

    @JvmStatic
    inline fun <reified T> String.decodeFromString(): T? {
        return Gson().fromJson(this, T::class.java)
    }
}