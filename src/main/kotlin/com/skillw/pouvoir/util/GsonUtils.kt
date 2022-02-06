package com.skillw.pouvoir.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder

object GsonUtils {
    @JvmStatic
    val gson: Gson by lazy {
        GsonBuilder()
            .enableComplexMapKeySerialization()
            .serializeNulls()
            .setPrettyPrinting()
            .create()
    }
}