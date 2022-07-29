package com.skillw.pouvoir.internal.annotation

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.Pouvoir.debug
import com.skillw.pouvoir.Pouvoir.scriptManager
import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.event.ManagerTime
import com.skillw.pouvoir.api.listener.Priority
import com.skillw.pouvoir.api.listener.ScriptListener
import com.skillw.pouvoir.api.manager.Manager.Companion.addExec
import com.skillw.pouvoir.api.manager.Manager.Companion.removeExec
import com.skillw.pouvoir.api.script.annotation.ScriptAnnotation
import com.skillw.pouvoir.api.script.annotation.ScriptAnnotationData
import com.skillw.pouvoir.internal.function.PouScriptFunction
import com.skillw.pouvoir.util.ClassUtils.findClass
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
        val script = data.script
        val path = script.key
        val args = data.args.toArgs()
        val function = data.function
        if (args.isEmpty() || args[0] == "") return
        val key = "$path::$function@Awake(${args[0]})"
        val time = ManagerTime(args[0])
        debug { "&aScript &6$key &ahas been registered!" }
        scriptManager.addExec(key, time) {
            val scriptToRun = scriptManager.search(path, true)
            if (scriptToRun == null || !scriptToRun.annotationData.containsKey(function)) {
                scriptManager.removeExec(key, time)
                return@addExec
            }
            debug { "&9Script &6$key &9is running!" }
            scriptManager.invoke<Unit>(scriptToRun, function, parameters = arrayOf(time.key.uppercase()))
        }
    }

}

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
        debug { "&aScriptListener &6$key &ahas been registered!" }
        script.onDeleted("Script-Listener-$key") {
            debug { "&cScriptListener &6$key &chas been unregistered!" }
            Pouvoir.listenerManager.remove(key)
        }
    }
}

/**
 * Inline Function
 *
 * @constructor Inline Function  Key(Optional)
 */
@AutoRegister
object Function : ScriptAnnotation("Function") {
    override fun handle(data: ScriptAnnotationData) {
        val script = data.script
        val args = data.args.toArgs()
        val function = data.function
        val key = if (args.isEmpty() || args[0] == "") function else args[0]
        PouScriptFunction(key, "${script.key}::$function").register()
        debug { "&aScript Inline Function &6$key &ahas been registered!" }
        script.onDeleted("Inline-Function-$key") {
            debug { "&cScript Inline Function &6$key &chas been unregistered!" }
            Pouvoir.inlineFunctionManager.remove(key)
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
        val script = data.script
        val args = data.args.toArgs()
        val function = data.function
        val key = if (args.isEmpty() || args[0] == "") function else args[0]
        object : ScriptAnnotation(key) {
            override fun handle(data: ScriptAnnotationData) {
                script.invoke(function, parameters = arrayOf(data))
            }
        }.register()
        debug { "&aScript Annotation &6$key &ahas been registered!" }
        script.onDeleted("Script-Annotation-$key") {
            debug { "&cScript Annotation &6$key &chas been unregistered!" }
            Pouvoir.scriptAnnotationManager.remove(key)
        }
    }
}

