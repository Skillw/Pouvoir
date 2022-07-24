package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.manager.sub.FunctionManager
import com.skillw.pouvoir.util.StringUtils.toArgs
import org.bukkit.entity.LivingEntity
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

    override fun onLoad() {
        register(object : PouFunction("TEST") {
            override fun predicate(args: Array<String>): Boolean {
                TODO("Not yet implemented")
            }

            override fun function(args: Array<String>): Any? {
                TODO("Not yet implemented")
            }
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

    override fun analysis(text: String): String {
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
        return analysis(matcher.appendTail(stringBuffer).toString())
    }


    @JvmStatic
    fun String.analysis(livingEntity: LivingEntity): String {
        return Pouvoir.functionManager.analysis(this)
    }


}