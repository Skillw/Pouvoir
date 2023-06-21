package com.skillw.pouvoir.internal.core.script.javascript

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.internal.manager.PouConfig
import com.skillw.pouvoir.util.script.MessageUtil
import com.skillw.pouvoir.util.staticClass
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang
import java.util.concurrent.ConcurrentHashMap

object JSStaticClass {

    private val staticClasses = ConcurrentHashMap<String, Any>()
    internal fun reloadStaticClasses() {
        val globalVariables = Pouvoir.scriptEngineManager.globalVariables
        staticClasses.forEach {
            globalVariables.remove(it.key)
        }
        staticClasses.clear()
        val script = PouConfig["script"]
        val staticSection = script.getConfigurationSection("static-classes")!!
        for (key in staticSection.getKeys(false)) {
            var path = staticSection[key].toString()
            val isObj = path.endsWith(";object")
            if (isObj)
                path = path.substringBeforeLast(";object")
            var staticClass = staticClass(path)
            if (staticClass == null) {
                console().sendLang("static-class-not-found", path)
                continue
            }
            if (isObj) {
                val clazz = staticClass.javaClass.getMethod("getRepresentedClass").invoke(staticClass) as Class<*>
                val field = clazz.getField("INSTANCE")
                staticClass = field.get(null)
                if (staticClass == null) {
                    MessageUtil.warning("The $path is not a object!")
                    continue
                }
            }
            staticClasses[key] = staticClass
        }
        staticClasses.forEach { (key, value) ->
            globalVariables[key] = value
        }
    }
}