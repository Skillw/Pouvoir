package com.skillw.pouvoir.internal.annotation

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.event.ScriptReloadEvent
import com.skillw.pouvoir.api.event.Time
import com.skillw.pouvoir.api.function.PouScriptFunction
import com.skillw.pouvoir.api.listener.ScriptListener
import com.skillw.pouvoir.api.script.annotation.ScriptAnnotation
import com.skillw.pouvoir.util.ClassUtils
import com.skillw.pouvoir.util.MapUtils.addSingle
import com.skillw.pouvoir.util.StringUtils.toArgs
import org.bukkit.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common5.Demand

/**
 * Awake
 *
 * @constructor When
 */
object Awake : ScriptAnnotation("Awake", handle@{ data ->
    val compiledFile = data.compiledFile
    val args = data.args.toArgs()
    val function = data.function
    if (args.isEmpty() || args[0] == "") return@handle
    Pouvoir.scriptManager.exec.addSingle(args[0]) {
        Pouvoir.scriptManager.invokePathWithFunction("${compiledFile.key}::$function")
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
    val clazz = ClassUtils.getEventClass(demand.get("event") ?: return@handle) ?: return@handle
    val priority = try {
        EventPriority.valueOf(demand.get("priority", "NORMAL").toString().uppercase())
    } catch (e: Exception) {
        EventPriority.NORMAL
    }
    val ignoreCancel = demand.tags.contains("--ignoreCancel")
    val key = "${compiledFile.key}::$function-${clazz.name}"
    ScriptListener.build(key, clazz, priority, ignoreCancel) { event ->
        compiledFile.invoke(function, argsMap = mutableMapOf("event" to event))
    }.register()
    Listener.listenerSet.add(key)
}, true) {
    private val listenerSet = HashSet<String>()

    @SubscribeEvent
    fun e(e: ScriptReloadEvent) {
        if (e.time == Time.BEFORE) {
            listenerSet.forEach {
                Pouvoir.listenerManager.remove(it)
            }
            listenerSet.clear()
        }
    }
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
    Function.functionSet.add(key)
}) {
    private val functionSet = HashSet<String>()

    @SubscribeEvent
    fun e(e: ScriptReloadEvent) {
        if (e.time == Time.BEFORE) {
            functionSet.forEach { Pouvoir.functionManager.remove(it) }
            functionSet.clear()
        }
    }
}

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
    Annotation.annotationSet.add(key)
}) {
    private val annotationSet = HashSet<String>()

    @SubscribeEvent
    fun e(e: ScriptReloadEvent) {
        if (e.time == Time.BEFORE) {
            annotationSet.forEach { Pouvoir.scriptAnnotationManager.remove(it) }
            annotationSet.clear()
        }
    }
}

