package com.skillw.pouvoir.internal.core.script.common.annotation

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.Pouvoir.scriptManager
import com.skillw.pouvoir.api.feature.placeholder.PouPlaceHolder
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import com.skillw.pouvoir.api.script.annotation.ScriptAnnotation
import com.skillw.pouvoir.api.script.annotation.ScriptAnnotationData
import com.skillw.pouvoir.internal.manager.PouConfig
import com.skillw.pouvoir.util.toArgs
import org.bukkit.entity.LivingEntity
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang

/**
 * PouPlaceholder
 *
 * @constructor PouPlaceholder Key Name Author Version
 */
@AutoRegister
internal object PouPlaceholder : ScriptAnnotation("PouPlaceholder") {
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
        PouConfig.debug { console().sendLang("annotation-pou-placeholder-register", key) }
        script.onDeleted("PouPlaceholder-$key") {
            PouConfig.debug { console().sendLang("annotation-pou-placeholder-unregister", key) }
            Pouvoir.placeholderManager.remove(key)
        }
    }
}