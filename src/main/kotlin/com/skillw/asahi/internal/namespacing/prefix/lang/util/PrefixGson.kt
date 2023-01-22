package com.skillw.asahi.internal.namespacing.prefix.lang.util

import com.google.gson.Gson
import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.prefixParser
import com.skillw.pouvoir.util.encodeJson
import com.skillw.pouvoir.util.findClass

internal object PrefixGson {
    @AsahiPrefix(["gson"], "lang")
    private fun gson() = prefixParser {
        when (val type = next()) {
            "encode" -> {
                val any = questAny()
                result {
                    any.get().encodeJson()
                }
            }

            "decode" -> {
                val gson = questString()
                expect("of")
                val clazz = next().findClass()
                result {
                    Gson().fromJson(gson.get(), clazz)
                }
            }

            else -> error("Unknown gson type $type")
        }
    }

}