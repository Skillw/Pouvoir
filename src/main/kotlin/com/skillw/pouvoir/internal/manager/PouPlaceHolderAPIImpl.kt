package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.PouvoirAPI.analysis
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

    override fun replace(entity: LivingEntity?, text: String, analysis: Boolean): String {
        var new = text
        entity ?: kotlin.run {
            return new.analysis()
        }
        val matcher = pattern.matcher(new)
        return if (!matcher.find()) {
            if (analysis) new.analysis() else new
        } else {
            val builder = StringBuffer()
            do {
                val identifier = matcher.group("identifier")
                val parameters = matcher.group("parameters")
                val rpgPlaceHolder = Pouvoir.pouPlaceholderManager[identifier]
                rpgPlaceHolder?.also {
                    val requested: String =
                        rpgPlaceHolder.onPlaceHolderRequest(parameters, entity, "0").toString()
                    matcher.appendReplacement(builder, requested)
                }
            } while (matcher.find())
            new = matcher.appendTail(builder).toString()
            if (entity is Player) {
                new = new.replacePlaceholder(entity)
            }
            new.run { if (analysis) analysis() else this }.colored()
        }
    }

    override fun replace(uuid: UUID, text: String): String {
        return replace(uuid.livingEntity(), text)
    }


}