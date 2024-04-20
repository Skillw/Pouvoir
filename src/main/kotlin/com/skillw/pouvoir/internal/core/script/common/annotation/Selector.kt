package com.skillw.pouvoir.internal.core.script.common.annotation

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.Pouvoir.selectorManager
import com.skillw.pouvoir.api.feature.selector.BaseSelector
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
 * Selector
 *
 * //@Selector(key)
 *
 * @constructor Key
 */
@AutoRegister
internal object Selector : ScriptAnnotation("Selector", fileAnnotation = true) {
    override fun handle(data: ScriptAnnotationData) {
        val script = data.script
        val args = data.args.toArgs()
        val function = data.function
        if (function != "null") return
        if (args.isEmpty()) return
        val key = args[0]
        object : BaseSelector(key) {

            override fun SelectorContext.select(caster: Target) {
                Pouvoir.scriptManager.invoke<Any?>(script, "select", parameters = arrayOf(this, caster))
            }

            override fun SelectorContext.filter(caster: Target) {
                Pouvoir.scriptManager.invoke<Any?>(script, "filter", parameters = arrayOf(this, caster))
            }

            override fun addParameter(dataMap: DataMap, vararg args: Any?) {
                Pouvoir.scriptManager.invoke<Any?>(script, "parameter", parameters = arrayOf(dataMap, arrayOf(*args)))
            }

        }.register()
        PouConfig.debug { console().sendLang("annotation-selector-register", key) }
        script.onDeleted("Selector-$key") {
            PouConfig.debug { console().sendLang("annotation-selector-unregister", key) }
            selectorManager.remove(key)
        }
    }
}