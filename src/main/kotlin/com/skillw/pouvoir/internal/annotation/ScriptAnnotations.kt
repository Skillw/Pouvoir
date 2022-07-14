package com.skillw.pouvoir.internal.annotation

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.Pouvoir.scriptManager
import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouScriptFunction
import com.skillw.pouvoir.api.listener.Priority
import com.skillw.pouvoir.api.listener.ScriptListener
import com.skillw.pouvoir.api.manager.Manager.Companion.addSingle
import com.skillw.pouvoir.api.script.annotation.ScriptAnnotation
import com.skillw.pouvoir.api.script.annotation.ScriptAnnotationData
import com.skillw.pouvoir.util.ClassUtils
import com.skillw.pouvoir.util.StringUtils.toArgs
import taboolib.common.platform.Platform
import taboolib.common.platform.event.EventPriority
import taboolib.common5.Demand

/**
 * Awake
 *
 * @constructor When( BeforeReload / Reload / enable / disable / active / load)
 */
@AutoRegister
object Awake : ScriptAnnotation("Awake") {
    override fun handle(data: ScriptAnnotationData) {
        val compiledFile = data.compiledFile
        val args = data.args.toArgs()
        val function = data.function
        if (args.isEmpty() || args[0] == "") return
        val key = args[0]
        scriptManager.addSingle(key) {
            scriptManager.invokePathWithFunction(
                "${compiledFile.key}::$function",
                argsMap = mutableMapOf("awakeType" to key.lowercase())
            )
        }
    }

}

/**
 * Listener
 *
 * @constructor -event <Class Name> -priority <Event Priority> -platform <Platform> --ignoreCancel
 */
@AutoRegister
object Listener : ScriptAnnotation("Listener") {
    override fun handle(data: ScriptAnnotationData) {
        val compiledFile = data.compiledFile
        val args = data.args
        val function = data.function
        if (args.isEmpty()) return
        val demand = Demand("dem $args")
        val clazz = ClassUtils.getClass(demand.get("event") ?: return) ?: return
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
        val key = "annotation-${compiledFile.key}::$function-${clazz.name}"
        ScriptListener.Builder(key, platform, clazz, Priority(level), ignoreCancel) { event ->
            compiledFile.invoke(function, argsMap = mutableMapOf("event" to event))
        }.build().register()
        compiledFile["reload-listener-$key"] = {
            Pouvoir.listenerManager.remove(key)
        }
    }
}

/**
 * Function
 *
 * @constructor Function Key(Optional)
 */
@AutoRegister
object Function : ScriptAnnotation("Function") {
    override fun handle(data: ScriptAnnotationData) {
        val compiledFile = data.compiledFile
        val args = data.args.toArgs()
        val function = data.function
        val key = if (args.isEmpty() || args[0] == "") function else args[0]
        PouScriptFunction(key, "${compiledFile.key}::$function").register()
        compiledFile["register-function-this::$function"] = {
            Pouvoir.functionManager.remove(key)
        }
    }
}

/**
 * Annotation
 *
 * @constructor Annotation Key(Optional)
 */
@AutoRegister
object Annotation : ScriptAnnotation("Annotation") {
    override fun handle(data: ScriptAnnotationData) {
        val compiledFile = data.compiledFile
        val args = data.args.toArgs()
        val function = data.function
        val key = if (args.isEmpty() || args[0] == "") function else args[0]
        object : ScriptAnnotation(key) {
            override fun handle(data: ScriptAnnotationData) {
                compiledFile.invoke(function, mutableMapOf("data" to data))
            }
        }.register()
        compiledFile["register-annotation-this::$function"] = {
            Pouvoir.scriptAnnotationManager.remove(key)
        }
    }
}

