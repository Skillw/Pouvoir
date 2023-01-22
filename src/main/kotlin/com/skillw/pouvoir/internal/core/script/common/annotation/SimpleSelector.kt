package com.skillw.pouvoir.internal.core.script.common.annotation

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.feature.selector.SimpleSelector
import com.skillw.pouvoir.api.feature.selector.Target
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import com.skillw.pouvoir.api.plugin.map.DataMap
import com.skillw.pouvoir.api.script.annotation.ScriptAnnotation
import com.skillw.pouvoir.api.script.annotation.ScriptAnnotationData
import com.skillw.pouvoir.internal.manager.PouConfig
import com.skillw.pouvoir.util.toArgs
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang

/**
 * SimpleSelector
 *
 * //@SimpleSelector(key)
 *
 * @constructor Key
 */
@AutoRegister
internal object SimpleSelector : ScriptAnnotation("SimpleSelector", fileAnnotation = true) {
    override fun handle(data: ScriptAnnotationData) {
        val script = data.script
        val args = data.args.toArgs()
        val function = data.function
        if (function != "null") return
        if (args.isEmpty()) return
        val key = args[0]
        object : SimpleSelector(key) {
            override fun SelectorContext.getTargets(caster: Target): Collection<Target> {
                return Pouvoir.scriptManager.invoke(script, "targets", parameters = arrayOf(this, caster))
                    ?: emptyList()
            }

            override fun addParameter(dataMap: DataMap, vararg args: Any?) {
                Pouvoir.scriptManager.invoke<Any?>(script, "filter", parameters = arrayOf(dataMap, arrayOf(*args)))
            }

        }.register()
        PouConfig.debug { console().sendLang("annotation-selector-register", key) }
        script.onDeleted("Selector-$key") {
            PouConfig.debug { console().sendLang("annotation-selector-unregister", key) }
            Pouvoir.selectorManager.remove(key)
        }
    }
}