package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.manager.sub.FunctionManager
import com.skillw.pouvoir.api.plugin.TotalManager
import com.skillw.pouvoir.internal.handle.PouFunctionHandle
import com.skillw.pouvoir.util.StringUtils.toArgs
import java.util.regex.Pattern

object FunctionManagerImpl : FunctionManager() {
    override val key = "FunctionManager"
    override val priority = 4
    override val subPouvoir = Pouvoir


    private lateinit var functionPattern: Pattern
    private val regexCache = StringBuilder()

    override fun register(value: PouFunction) {
        register(value.key, value)
    }

    override fun onInit() {
        TotalManager.forEachClass {
            PouFunctionHandle.inject(it)
        }
    }

    override fun onLoad() {
        register(PouFunction("TEST", { false }) {
            null
        })

    }

    override fun register(key: String, value: PouFunction) {
        map[key] = value
        if (regexCache.isNotEmpty()) {
            regexCache.append("|")
        }
        regexCache.append(key)
        val regex = "($regexCache)\\(([^()]*)\\)"
        functionPattern = Pattern.compile(regex, 2)
    }

    private fun replace(text: String): String {
        val matcher = functionPattern.matcher(text)
        if (!matcher.find()) return text
        val stringBuffer = StringBuffer()
        do {
            val func = map[matcher.group(1)] ?: continue
            val args = analysis(matcher.group(2))
            val result = func.apply(args.toArgs()).toString()
            matcher.appendReplacement(stringBuffer, result)
            Pouvoir.debug("$func(${matcher.group(2)}) -> $result")
        } while (matcher.find())
        return replace(matcher.appendTail(stringBuffer).toString())
    }

    override fun analysis(text: String): String {
        return replace(text)
    }

    override fun onReload() {

    }


}