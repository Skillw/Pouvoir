package com.skillw.pouvoir.internal.core.script.common.annotation

import com.skillw.pouvoir.Pouvoir.scriptManager
import com.skillw.pouvoir.api.plugin.ManagerTime
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import com.skillw.pouvoir.api.script.annotation.ScriptAnnotation
import com.skillw.pouvoir.api.script.annotation.ScriptAnnotationData
import com.skillw.pouvoir.internal.manager.PouConfig.debug
import com.skillw.pouvoir.util.toArgs
import taboolib.common.platform.function.console
import taboolib.common.platform.function.submitAsync
import taboolib.module.lang.sendLang

/**
 * Awake
 *
 * @constructor When( BeforeReload / Reload / enable / disable / active /
 *     load)
 */
@AutoRegister
internal object Awake : ScriptAnnotation("Awake") {

    override fun handle(data: ScriptAnnotationData) {
        val script = data.script
        val path = script.key
        val args = data.args.toArgs()
        val function = data.function
        if (args.isEmpty() || args[0] == "") return
        val key = "$path::$function@Awake(${args[0]})"
        val time = ManagerTime(args[0])
        debug { console().sendLang("annotation-awake-register", key) }
        scriptManager.addExec(time, key) {
            val scriptToRun = scriptManager.search(path)
            if (scriptToRun == null || !scriptToRun.annotationData.containsKey(function)) {
                debug { console().sendLang("annotation-awake-unregister", key) }
                scriptManager.removeExec(time, key)
                return@addExec
            }
            debug { console().sendLang("annotation-awake-running", key) }
            submitAsync {
                scriptManager.invoke<Any?>(scriptToRun, function, parameters = arrayOf(time.key.uppercase()))
            }
        }
    }

}