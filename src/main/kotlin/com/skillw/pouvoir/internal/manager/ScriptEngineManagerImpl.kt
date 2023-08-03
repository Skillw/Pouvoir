package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.PouvoirAPI
import com.skillw.pouvoir.api.manager.sub.script.ScriptEngineManager
import com.skillw.pouvoir.api.script.engine.PouScriptEngine
import com.skillw.pouvoir.util.ClassUtils
import com.skillw.pouvoir.util.MessageUtils
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang
import java.util.concurrent.ConcurrentHashMap

object ScriptEngineManagerImpl : ScriptEngineManager() {
    override val key = "ScriptEngineManager"
    override val priority: Int = 5
    override val subPouvoir = Pouvoir
    private val suffixMap = ConcurrentHashMap<String, PouScriptEngine>()
    override val globalVariables: MutableMap<String, Any> = ConcurrentHashMap()
    internal val relocates = HashMap<String, String>()

    private val staticClasses = ConcurrentHashMap<String, Any>()


    override fun onLoad() {
        relocate(">taboolib.", "com.skillw.pouvoir.taboolib.")
        reloadStaticClasses()
    }

    override fun onReload() {
        reloadStaticClasses()
    }

    override fun put(key: String, value: PouScriptEngine): PouScriptEngine? {
        for (suffix in value.suffixes) {
            suffixMap[suffix] = value
        }
        return super.put(key, value)
    }

    override fun getEngine(suffix: String): PouScriptEngine? {
        return suffixMap[suffix]
    }

    override fun relocate(from: String, to: String) {
        relocates[from] = to
    }

    override fun deleteRelocate(from: String) {
        relocates.remove(from)
    }

    private fun reloadStaticClasses() {
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
            path = path.replace(";object", "")
            var staticClass = ClassUtils.staticClass(path)
            if (staticClass == null) {
                console().sendLang("static-class-not-found", path)
                continue
            }
            if (isObj) {
                val clazz = staticClass.javaClass.getMethod("getRepresentedClass").invoke(staticClass) as Class<*>
                val field = clazz.getField("INSTANCE")
                staticClass = field.get(null)
                if (staticClass == null) {
                    MessageUtils.warning("The $path is not a object!")
                    continue
                }
            }
            staticClasses[key] = staticClass
        }
        staticClasses.forEach { (key, value) ->
            globalVariables[key] = value
        }
        globalVariables["PouvoirAPI"] = PouvoirAPI
    }
}