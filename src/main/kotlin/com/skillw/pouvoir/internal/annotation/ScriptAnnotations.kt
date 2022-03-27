package com.skillw.pouvoir.internal.annotation

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.Pouvoir.scriptManager
import com.skillw.pouvoir.api.function.PouScriptFunction
import com.skillw.pouvoir.api.listener.ScriptListener
import com.skillw.pouvoir.api.manager.Manager.Companion.addSingle
import com.skillw.pouvoir.api.script.annotation.ScriptAnnotation
import com.skillw.pouvoir.util.ClassUtils
import com.skillw.pouvoir.util.StringUtils.toArgs
import taboolib.common.platform.event.EventPriority
import taboolib.common5.Demand

/**
 * Awake
 *
 * @constructor When( BeforeReload / Reload / enable / disable / active / load)
 */
object Awake : ScriptAnnotation("Awake", handle@{ data ->
    val compiledFile = data.compiledFile
    val args = data.args.toArgs()
    val function = data.function
    if (args.isEmpty() || args[0] == "") return@handle
    val key = args[0]
    scriptManager.addSingle(key) {
        scriptManager.invokePathWithFunction(
            "${compiledFile.key}::$function",
            argsMap = mutableMapOf("awakeType" to key.lowercase())
        )
    }
})

/**
 * Listener
 *
 * @constructor -event <Class Name> -priority <Event Priority> --ignoreCancel
 */
object Listener : ScriptAnnotation("Listener", handle@{ data ->
    val compiledFile = data.compiledFile
    val args = data.args
    val function = data.function
    if (args.isEmpty()) return@handle
    val demand = Demand("dem $args")
    val clazz = ClassUtils.getClass(demand.get("event") ?: return@handle) ?: return@handle
    val priority = try {
        EventPriority.valueOf(demand.get("priority", "NORMAL").toString().uppercase())
    } catch (e: Exception) {
        EventPriority.NORMAL
    }
    val ignoreCancel = demand.tags.contains("--ignoreCancel")
    val key = "annotation-${compiledFile.key}::$function-${clazz.name}"
    ScriptListener.build(key, clazz, priority, ignoreCancel) { event ->
        compiledFile.invoke(function, argsMap = mutableMapOf("event" to event))
    }.register()
    compiledFile["reload-listener-$key"] = {
        Pouvoir.listenerManager.remove(key)
    }
}, true) {
}

/**
 * Function
 *
 * @constructor Function Key(Optional)
 */
object Function : ScriptAnnotation("Function", handle@{ data ->
    val compiledFile = data.compiledFile
    val args = data.args.toArgs()
    val function = data.function
    val key = if (args.isEmpty() || args[0] == "") function else args[0]
    PouScriptFunction(key, "${compiledFile.key}::$function").register()
    compiledFile["register-function-this::$function"] = {
        Pouvoir.functionManager.remove(key)
    }
})

/**
 * Annotation
 *
 * @constructor Annotation Key(Optional)
 */
object Annotation : ScriptAnnotation("Annotation", handle@{ data ->
    val compiledFile = data.compiledFile
    val args = data.args.toArgs()
    val function = data.function
    val key = if (args.isEmpty() || args[0] == "") function else args[0]
    val annotation = object : ScriptAnnotation(key, { compiledFile.invoke(function, mutableMapOf("data" to it)) }) {}
    annotation.register()
    compiledFile["register-annotation-this::$function"] = {
        Pouvoir.scriptAnnotationManager.remove(key)
    }
})

