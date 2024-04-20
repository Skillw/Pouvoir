package com.skillw.asahi.internal.namespacing.prefix.lang

import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.prefixParser
import com.skillw.asahi.api.quest
import com.skillw.asahi.api.quester
import com.skillw.asahi.api.script.linking.NativeFunction
import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.util.toArgs
import taboolib.common.platform.ProxyCommandSender


private val funcRegex = Regex(".*?([a-zA-Z_$]+).*?\\((.*?)\\).*?")

@AsahiPrefix(["fun", "def"], "lang")
private fun `fun`() = prefixParser<NativeFunction> {
    val result = funcRegex.find(splitBeforeString("{"))!!
    val name = result.groups[1]?.value!!
    val params = result.groups[2]?.value!!.toArgs().filter { it.isNotEmpty() && it.isNotBlank() }.toTypedArray()
    val content = parseScript()
    result {
        NativeFunction.create(name, params, content).also { context().addInvoker(name, it) }
    }
}

@AsahiPrefix(["invoke", "call"], "lang")
private fun invoke() = prefixParser<Any?> {
    val func = quest<String>()
    val paramsGetter = if (peek() == "[") quest<Array<Any>>() else quester { emptyArray() }
    result {
        val params = paramsGetter.get()
        invoke(func.get(), *params)
    }
}

/**
 *
 * invokeScript {
 *  path = "xxx.js"
 *  function = main
 * } [ args1 args2 ]
 *
 *
 *
 */
@AsahiPrefix(["invokeScript"], "lang")
private fun invokeJs() = prefixParser<Any?> {
    val map = if (peek() == "[" || peek() == "{") questTypeMap() else quester { emptyMap<String, Any>() }
    val path = map.quester { it["path"] as String }
    val function = map.quester { it["function"] as String }
    val parameters = questList()
    result {
        println(parameters.get())
        Pouvoir.scriptManager.invoke(path.get(), function.get() , emptyMap(), null, *parameters.get().toTypedArray())
    }
}


@AsahiPrefix(["import"], "lang")
private fun import() = prefixParser<Unit> {
    val paths = if (peek() == "[") quest() else quest<String>().quester { arrayOf(it) }
    result {
        import(*paths.get())
    }
}


