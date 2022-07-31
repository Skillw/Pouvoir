package com.skillw.pouvoir.internal.annotation

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.listener.Priority
import com.skillw.pouvoir.api.listener.ScriptListener
import com.skillw.pouvoir.api.script.annotation.ScriptAnnotation
import com.skillw.pouvoir.api.script.annotation.ScriptAnnotationData
import com.skillw.pouvoir.internal.manager.PouConfig
import com.skillw.pouvoir.util.ClassUtils.findClass
import taboolib.common.platform.Platform
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.function.console
import taboolib.common5.Demand
import taboolib.module.lang.sendLang

/**
 * Listener
 *
 * @constructor -event <Class Name> -priority <Event Priority> -platform <Platform> --ignoreCancel
 */
@AutoRegister
object Listener : ScriptAnnotation("Listener", true) {
    override fun handle(data: ScriptAnnotationData) {
        val script = data.script
        val args = data.args
        val function = data.function
        if (args.isEmpty()) return
        val demand = Demand("dem $args")
        val clazz = (demand.get("event") ?: return).findClass() ?: return
        val level: Int = try {
            EventPriority.valueOf(demand.get("priority", "NORMAL").toString().uppercase()).level
        } catch (e: Exception) {
            demand.get("priority", "0")?.toIntOrNull() ?: 0
        }
        val platform: Platform = try {
            Platform.valueOf(demand.get("platform", "BUKKIT").toString().uppercase())
        } catch (e: Exception) {
            Platform.BUKKIT
        }
        val ignoreCancel = demand.tags.contains("--ignoreCancel")
        val key = "${script.key}::$function-${clazz.simpleName}"
        ScriptListener.Builder(key, platform, clazz, Priority(level), ignoreCancel) { event ->
            script.invoke(function, parameters = arrayOf(event))
        }.build().register()
        PouConfig.debug { console().sendLang("annotation-listener-register", key) }
        script.onDeleted("Script-Listener-$key") {
            PouConfig.debug { console().sendLang("annotation-listener-unregister", key) }
            Pouvoir.listenerManager.remove(key)
        }
    }
}

