package com.skillw.pouvoir.internal.core.script.common.annotation

import com.skillw.asahi.api.AsahiManager
import com.skillw.asahi.api.member.parser.infix.namespacing.BaseInfix
import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import com.skillw.pouvoir.api.script.annotation.ScriptAnnotation
import com.skillw.pouvoir.api.script.annotation.ScriptAnnotationData
import com.skillw.pouvoir.internal.manager.PouConfig
import com.skillw.pouvoir.util.staticClass
import com.skillw.pouvoir.util.toArgs
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang

/**
 * Asahi Infix
 *
 * @constructor Asahi Infix
 */
@AutoRegister
internal object AsahiInfix : ScriptAnnotation("AsahiInfix") {

    override fun handle(data: ScriptAnnotationData) {
        val script = data.script
        val args = data.args.toArgs()
        val function = data.function
        if (args.isEmpty()) error("At least 2 arguments: key , typeClass , (namespace)")
        val key = args[0]
        val type: Class<out Any> = staticClass(args[1]) as? Class<out Any> ?: return
        val namespace = args.getOrElse(2) { "common" }

        BaseInfix.infix(type, key, namespace = namespace) {
            Pouvoir.scriptManager.invoke(
                script,
                function,
                parameters = arrayOf(this, it)
            )
        }

        PouConfig.debug { console().sendLang("annotation-asahi-infix-register", key) }
        script.onDeleted("Asahi-Infix-$key") {
            PouConfig.debug { console().sendLang("annotation-asahi-infix-unregister", key) }
            AsahiManager.getNamespace(namespace).infixMap.remove(type)
        }
    }
}