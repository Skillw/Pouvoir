package com.skillw.pouvoir.internal.core.script.common.annotation

import com.skillw.pouvoir.Pouvoir.scriptManager
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import com.skillw.pouvoir.api.script.annotation.ScriptAnnotation
import com.skillw.pouvoir.api.script.annotation.ScriptAnnotationData
import com.skillw.pouvoir.internal.manager.PouConfig
import com.skillw.pouvoir.util.toArgs
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.entity.Player
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang

/**
 * Placeholder
 *
 * @constructor Placeholder Key Name Author Version
 */
@AutoRegister
internal object Placeholder : ScriptAnnotation("Placeholder") {
    override fun handle(data: ScriptAnnotationData) {
        val script = data.script
        val args = data.args.toArgs()
        val function = data.function
        if (args.size < 4) return
        val key = args[0]
        val name: String = args[1]
        val author: String = args[2]
        val version: String = args[3]
        object : PlaceholderExpansion() {
            override fun onPlaceholderRequest(player: Player?, params: String): String {
                return scriptManager.invoke<String>(script, function, parameters = arrayOf(params, player)).toString()
            }

            override fun getIdentifier(): String {
                return key
            }

            override fun getName(): String {
                return name
            }

            override fun getAuthor(): String {
                return author
            }

            override fun getVersion(): String {
                return version
            }
        }.register()
        PouConfig.debug { console().sendLang("annotation-pou-placeholder-register", key) }
    }
}