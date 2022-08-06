package com.skillw.pouvoir.internal.annotation

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.Pouvoir.scriptManager
import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.placeholder.PouPlaceHolder
import com.skillw.pouvoir.api.script.annotation.ScriptAnnotation
import com.skillw.pouvoir.api.script.annotation.ScriptAnnotationData
import com.skillw.pouvoir.internal.manager.PouConfig
import com.skillw.pouvoir.util.StringUtils.toArgs
import org.bukkit.entity.LivingEntity
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang

/**
 * Placeholder
 *
 * @constructor Placeholder Key Name Author Version
 */
@AutoRegister
object Placeholder : ScriptAnnotation("Placeholder") {
    override fun handle(data: ScriptAnnotationData) {
        val script = data.script
        val args = data.args.toArgs()
        val function = data.function
        if (args.size < 4) return
        val key = args[0]
        val name: String = args[1]
        val author: String = args[2]
        val version: String = args[3]
        object : PouPlaceHolder(key, name, author, version) {
            override fun onPlaceHolderRequest(params: String, entity: LivingEntity, def: String): String? {
                return scriptManager.invoke<String>(script, function, parameters = arrayOf(params, entity, def))
            }
        }.register()
        PouConfig.debug { console().sendLang("annotation-placeholder-register", key) }
        script.onDeleted("Inline-Function-$key") {
            PouConfig.debug { console().sendLang("annotation-placeholder-unregister", key) }
            Pouvoir.pouPlaceholderManager.remove(key)
        }
    }
}