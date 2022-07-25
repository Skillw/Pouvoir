package com.skillw.pouvoir.internal.annotation

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.Pouvoir.debug
import com.skillw.pouvoir.Pouvoir.scriptManager
import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.event.ManagerTime
import com.skillw.pouvoir.api.listener.Priority
import com.skillw.pouvoir.api.listener.ScriptListener
import com.skillw.pouvoir.api.manager.Manager.Companion.addExec
import com.skillw.pouvoir.api.script.annotation.ScriptAnnotation
import com.skillw.pouvoir.api.script.annotation.ScriptAnnotationData
import com.skillw.pouvoir.internal.function.PouScriptFunction
import com.skillw.pouvoir.util.ClassUtils.findClass
import com.skillw.pouvoir.util.StringUtils.toArgs
import taboolib.common.platform.Platform
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.function.warning
import taboolib.common5.Coerce
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
        val time = Coerce.toEnum(args[0].uppercase(), ManagerTime::class.java)
        if (time == null) {
            warning("No such ManagerTime called '${args[0]}',this is from $path::$function !")
            warning("Usable ManagerTime: ${ManagerTime.values().map { it.name.lowercase() }}")
            return
        }
        debug("&aScript &6$key &ahas been registered!")
        scriptManager.addExec(key, time) {
            debug("&9Script &6$key &9is running!")
            val scriptToRun = scriptManager.search(path)
            if (scriptToRun != null && scriptToRun.annotationData.containsKey(function))
                scriptManager.invoke<Unit>(
                    "$path::$function",
                    variables = mutableMapOf("awakeType" to key.lowercase())
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
        val key = "annotation-${script.key}::$function-${clazz.name}"
        ScriptListener.Builder(key, platform, clazz, Priority(level), ignoreCancel) { event ->
            script.invoke(function, variables = mutableMapOf("event" to event))
        }.build().register()
        debug("&aScript ScriptListener &6$key &ahas been registered!")
        script.onRemove {
            debug("&cScript ScriptListener &6$key &chas been unregistered!")
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
        debug("&aScript Inline Function &6$key &ahas been registered!")
        script.onRemove {
            debug("&cScript Inline Function &6$key &chas been unregistered!")
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
                script.invoke(function, mutableMapOf("data" to data))
            }
        }.register()
        debug("&aScript Annotation &6$key &ahas been registered!")
        script.onRemove {
            debug("&cScript Annotation &6$key &chas been unregistered!")
            Pouvoir.scriptAnnotationManager.remove(key)
        }
    }
}

