package com.skillw.pouvoir.internal.core.script.common.annotation

import com.skillw.asahi.api.AsahiManager
import com.skillw.asahi.api.member.context.AsahiContext
import com.skillw.asahi.api.member.lexer.AsahiDemand
import com.skillw.asahi.api.member.quest.Quester
import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import com.skillw.pouvoir.api.script.annotation.ScriptAnnotation
import com.skillw.pouvoir.api.script.annotation.ScriptAnnotationData
import com.skillw.pouvoir.internal.manager.PouConfig
import com.skillw.pouvoir.util.toArgs
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang
import java.util.function.Function

/**
 * Asahi Prefix
 *
 * //@AsahiPrefix(-name key1,key2,key3 -namespace common)
 *
 * @constructor Key alias namespaces
 */
@AutoRegister
internal object AsahiPrefix : ScriptAnnotation("AsahiPrefix") {

    override fun handle(data: ScriptAnnotationData) {
        val script = data.script
        val args = data.args
        val function = data.function
        val demand = AsahiDemand.of(args)
        val names = demand.get("name", "").toArgs()
        val namespace = demand.get("namespace", "common")

        com.skillw.asahi.api.prefixParser {
            Pouvoir.scriptManager.invoke<Quester<Any?>>(
                script,
                function,
                parameters = arrayOf(this),
                arguments = mapOf("result" to Function<AsahiContext.() -> Any?, Quester<Any?>> {
                    result { it() }
                })
            ) ?: Quester { null }
        }.register(names.first(), *names, namespace)

        PouConfig.debug { console().sendLang("annotation-asahi-prefix-register", key) }
        script.onDeleted("Asahi-Prefix-$key") {
            PouConfig.debug { console().sendLang("annotation-asahi-prefix-unregister", key) }
            AsahiManager.getNamespace(namespace).prefixMap.remove(key)
        }
    }
}