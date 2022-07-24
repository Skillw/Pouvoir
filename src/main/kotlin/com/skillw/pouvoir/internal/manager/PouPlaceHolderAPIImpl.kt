package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.manager.sub.PouPlaceHolderAPI
import com.skillw.pouvoir.util.EntityUtils.livingEntity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import taboolib.module.chat.colored
import taboolib.platform.compat.replacePlaceholder
import java.util.*
import java.util.regex.Pattern

object PouPlaceHolderAPIImpl : PouPlaceHolderAPI {
    override val key = "PouPlaceHolderAPI"
    override val priority = 1
    override val subPouvoir = Pouvoir

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
        livingEntity ?: kotlin.run {
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
                val rpgPlaceHolder = Pouvoir.pouPlaceholderManager[identifier]
                rpgPlaceHolder?.also {
                    val requested: String =
                        rpgPlaceHolder.onPlaceHolderRequest(parameters, livingEntity, "0").toString()
                    matcher.appendReplacement(builder, requested)
                }
            } while (matcher.find())
            new = matcher.appendTail(builder).toString()
            if (livingEntity is Player) {
                new = new.replacePlaceholder(livingEntity)
            }
            analysis(new).colored()
        }
    }

    override fun replace(uuid: UUID, text: String): String {
        return replace(uuid.livingEntity(), text)
    }

    private fun analysis(text: String): String {
        return Pouvoir.functionManager.analysis(text)
    }


}