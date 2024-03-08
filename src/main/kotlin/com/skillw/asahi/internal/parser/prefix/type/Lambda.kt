package com.skillw.asahi.internal.parser.prefix.type

import com.skillw.asahi.api.annotation.AsahiTypeParser
import com.skillw.asahi.api.quester
import com.skillw.asahi.api.script.linking.NativeFunction
import com.skillw.asahi.api.typeParser
import com.skillw.pouvoir.util.toArgs

/**
 * @className Lambda
 *
 * @author Glom
 * @date 2023/1/20 23:54 Copyright 2024 Glom.
 */
/**
 * TODO
 *
 * (变量1,变量2) -> { 处理 }
 *
 * (变量1,变量2) => { 处理 }
 */
private val paramRegex = Regex("\\((?<params>.*?)\\)")

@AsahiTypeParser
fun lambda() = typeParser(NativeFunction::class.java) {
    val phrase = splitBeforeString("{")
    val params =
        paramRegex.find(phrase)!!.groups["params"]!!.value.toArgs().filter { it.isNotEmpty() && it.isNotBlank() }
            .toTypedArray()
    val content = parseScript(*namespaceNames())
    val key = (params.hashCode() + content.hashCode()).toString()
    quester {
        NativeFunction.create(key, params, content)
    }
}
