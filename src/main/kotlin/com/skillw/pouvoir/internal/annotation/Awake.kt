package com.skillw.pouvoir.internal.annotation

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.event.ManagerTime
import com.skillw.pouvoir.api.manager.Manager.Companion.addExec
import com.skillw.pouvoir.api.manager.Manager.Companion.removeExec
import com.skillw.pouvoir.api.script.annotation.ScriptAnnotation
import com.skillw.pouvoir.api.script.annotation.ScriptAnnotationData
import com.skillw.pouvoir.internal.manager.PouConfig.debug
import com.skillw.pouvoir.util.StringUtils.toArgs
import taboolib.common.platform.function.console
import taboolib.module.lang.asLangText
import taboolib.module.lang.sendLang

/**
 * Awake
 *
 * @constructor When( BeforeReload / Reload / enable / disable / active /
 *     load)
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
        debug { console().sendLang("annotation-awake-register", key) }
        Pouvoir.scriptManager.addExec(key, time) {
            val scriptToRun = Pouvoir.scriptManager.search(path, true)
            if (scriptToRun == null || !scriptToRun.annotationData.containsKey(function)) {
                debug { console().sendLang("annotation-awake-unregister", key) }
                Pouvoir.scriptManager.removeExec(key, time)
                return@addExec
            }
            debug { console().sendLang("annotation-awake-running", key) }
            val start = System.currentTimeMillis()
            val result =
                Pouvoir.scriptManager.invoke<Any?>(scriptToRun, function, parameters = arrayOf(time.key.uppercase()))
                    .run { if (this is Unit) console().asLangText("kotlin-unit") else toString() }
            val end = System.currentTimeMillis()
            console().sendLang("command-script-invoke-end", path, result, (end - start))
        }
    }

}