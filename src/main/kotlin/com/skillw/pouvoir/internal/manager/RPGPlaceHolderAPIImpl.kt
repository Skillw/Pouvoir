package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.manager.sub.RPGPlaceHolderAPI
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import taboolib.module.chat.colored
import java.util.*
import java.util.regex.Pattern

object RPGPlaceHolderAPIImpl : RPGPlaceHolderAPI {
    override val key = "RPGPlaceHolderAPI"
    override val priority = 1

    private val pattern = Pattern.compile(
        String.format(
            "\\%s((?<identifier>[a-zA-Z0-9]+)_)(?<parameters>[^%s%s]+)\\%s",
            "%",
            "%",
            "%",
            "%"
        )
    )

    override fun replace(livingEntity: LivingEntity?, text: String): String {
        var new = text
        if (livingEntity == null) {
            return analysis(new)
        }
        val matcher = pattern.matcher(new)
        return if (!matcher.find()) {
            analysis(new)
        } else {
            val builder = StringBuffer()
            do {
                val identifier = matcher.group("identifier")
                val parameters = matcher.group("parameters")
                val rpgPlaceHolder = Pouvoir.placeholderDataManager[identifier]
                if (rpgPlaceHolder != null) {
                    val requested: String =
                        rpgPlaceHolder.onPlaceHolderRequest(parameters, livingEntity, "0").toString()
                    matcher.appendReplacement(builder, requested)
                }
            } while (matcher.find())
            new = matcher.appendTail(builder).toString()
            if (livingEntity is Player) {
                new = PlaceholderAPI.setPlaceholders(livingEntity, new)
            }
            analysis(new).colored()
        }
    }

    override fun replace(uuid: UUID, text: String): String {
        TODO("Not yet implemented")
    }

    private fun analysis(text: String): String {
        return Pouvoir.functionManager.analysis(text).toString()
    }

}