package com.skillw.pouvoir.internal.core.script.common.annotation

import com.skillw.asahi.api.member.lexer.AsahiDemand
import com.skillw.asahi.util.cast
import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.Pouvoir.scriptManager
import com.skillw.pouvoir.api.plugin.ManagerTime
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import com.skillw.pouvoir.api.script.annotation.ScriptAnnotation
import com.skillw.pouvoir.api.script.annotation.ScriptAnnotationData
import com.skillw.pouvoir.internal.feature.listener.CustomListener
import com.skillw.pouvoir.internal.manager.PouConfig
import com.skillw.pouvoir.util.existClass
import com.skillw.pouvoir.util.findClass
import taboolib.common.platform.Platform
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang

/**
 * Listener
 *
 * @constructor -event <Class Name> -priority <Event Priority> -platform
 *     <Platform> --ignoreCancel
 */
@AutoRegister
internal object Listener : ScriptAnnotation("Listener", true) {
    override fun handle(data: ScriptAnnotationData) {
        val script = data.script
        val args = data.args
        val function = data.function
        if (args.isEmpty()) return
        val demand = AsahiDemand.of(args)
        demand.get("bind", "").run {
            if (!isEmpty() && !existClass()) return
        }
        val clazz = demand.getString("event").findClass() ?: return
        val priority = demand.get("priority", "NORMAL").cast<EventPriority>()
        val platform = Platform.BUKKIT
        val ignoreCancel = demand.tagAnyOf("ignoreCancel", "ignoreCancelled")
        val onActive = demand.tagAnyOf("onActive", "active")
        val key = "${script.key}::$function-${clazz.simpleName}"
        CustomListener.Builder(key, platform, clazz, priority, ignoreCancel) { event ->
            scriptManager.invoke<Unit>(script, function, parameters = arrayOf(event))
        }.build().apply {
            if (onActive) {
                scriptManager.addExec(ManagerTime.ACTIVE, "Script-Listener-$key") {
                    register()
                }
            } else register()
        }
        PouConfig.debug { console().sendLang("annotation-listener-register", key) }
        script.onDeleted("Script-Listener-$key") {
            PouConfig.debug { console().sendLang("annotation-listener-unregister", key) }
            Pouvoir.listenerManager.remove(key)
        }
    }
}

