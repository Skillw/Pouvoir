package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.manager.sub.FunctionManager
import com.skillw.pouvoir.api.plugin.TotalManager
import com.skillw.pouvoir.internal.handle.PouFunctionHandle
import com.skillw.pouvoir.util.StringUtils.toArgs
import java.util.regex.Pattern

object FunctionManagerImpl : FunctionManager() {
    override val key = "FunctionManager"
    override val priority = 4


    private lateinit var functionPattern: Pattern
    private val regexCache = StringBuilder()

    override fun register(value: PouFunction) {
        register(value.key, value)
    }

    override fun init() {
        TotalManager.forEachClass {
            PouFunctionHandle.inject(it)
        }
    }

    override fun load() {
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
        val regex = "($regexCache)\\((.*)\\)"
        functionPattern = Pattern.compile(regex, 2)
    }

    override fun analysis(text: String): String {
        var result = text
        var matcher = functionPattern.matcher(result)
        while (matcher.find()) {
            val func = map[matcher.group(1)] ?: continue
            val args = analysis(matcher.group(2))
            result = result.replace(matcher.group(0), func.apply(args.toArgs()).toString())
            matcher = functionPattern.matcher(result)
        }
        return result
    }

    override fun reload() {
        
    }


}