package com.skillw.pouvoir.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.function.Function
import com.skillw.pouvoir.api.manager.sub.FunctionManager
import org.bukkit.entity.LivingEntity
import java.util.*
import java.util.regex.Pattern

object FunctionManagerImpl : FunctionManager() {
    override val key = "FunctionManager"
    private lateinit var functionPattern: Pattern
    private val regexCache = StringBuilder()

    override fun register(func: Function) {
        register(func.key, func)
    }

    override fun register(key: String, func: Function) {
        map[key] = func
        if (regexCache.isNotEmpty()) {
            regexCache.append("|")
        }
        regexCache.append(key)
        val regex = "($regexCache)\\((.*)\\)"
        functionPattern = Pattern.compile(regex, 2)
    }

    fun toArgs(str: String): Array<String> =
        if (str.contains(",")) str.replace(" ", "").split(",").toTypedArray() else arrayOf(
            str.replace(" ", "")
        )

    override fun analysis(text: String): String {
        var result = text
        var matcher = functionPattern.matcher(result)
        while (matcher.find()) {
            val func = map[matcher.group(1)]
            val args = matcher.group(2)
            result = result.replace(matcher.group(0), func!!.apply(toArgs(args)).toString())
            matcher = functionPattern.matcher(result)
        }
        return result
    }

    override fun analysis(entity: LivingEntity, text: String): String {
        return analysis(Pouvoir.rpgPlaceHolderAPI.replace(entity, text))
    }

    override fun analysis(uuid: UUID, text: String): String {
        return analysis(Pouvoir.rpgPlaceHolderAPI.replace(uuid, text))
    }

    override fun reload() {

    }


}