package com.skillw.asahi.internal.namespacing.prefix.lang.linking

import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.prefixParser
import com.skillw.asahi.api.quest
import com.skillw.asahi.api.quester
import com.skillw.pouvoir.util.findClass
import com.skillw.pouvoir.util.plugin.PluginUtils.getClasses
import taboolib.library.reflex.Reflex.Companion.invokeConstructor

/**
 * @className Java
 *
 * @author Glom
 * @date 2023/1/18 18:19 Copyright 2023 user. All rights reserved.
 */
@AsahiPrefix(["java"], "lang")
private fun java() = prefixParser<Any?> {
    val isPackage = expect("in")
    expect("of")
    val path = questString()
    result {
        if (isPackage) {
            getClasses(path.get()).forEach {
                context()[it.simpleName] = it
            }
        } else {
            path.get().findClass()
        }
    }
}

@AsahiPrefix(["new"], "lang")
private fun new() = prefixParser<Any> {
    val clazz = next().let { quester { context()[it] as Class<*> } }
    val paramsGetter = if (peek() == "[" || peek() == "(") quest() else quester { emptyList<Any?>() }
    expect("[]", "()")
    result {
        val params = paramsGetter.get()
        clazz.get().invokeConstructor(*params.toTypedArray())
    }
}