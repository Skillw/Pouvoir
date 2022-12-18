package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.manager.sub.script.ScriptEngineManager
import com.skillw.pouvoir.api.script.engine.PouScriptEngine
import com.skillw.pouvoir.internal.core.script.common.top.TopLevel
import com.skillw.pouvoir.internal.manager.PouConfig.getStaticClassesInfo
import taboolib.common.platform.function.info
import taboolib.module.chat.colored
import java.util.concurrent.ConcurrentHashMap
import javax.script.ScriptContext
import javax.script.ScriptContext.ENGINE_SCOPE

object ScriptEngineManagerImpl : ScriptEngineManager() {
    override val key = "ScriptEngineManager"
    override val priority: Int = 5
    override val subPouvoir = Pouvoir
    private val suffixMap = ConcurrentHashMap<String, PouScriptEngine>()
    override val globalVariables: MutableMap<String, Any> = ConcurrentHashMap()
    internal val relocates = HashMap<String, String>()
    internal fun ScriptContext.addCheckVarsFunc() {
        setAttribute(
            "globalVars",
            Runnable {
                getStaticClassesInfo().map(String::colored).forEach(::info)
                TopLevel.getInfo().map(String::colored).forEach(::info)
            }, ENGINE_SCOPE
        )
    }

    override fun onLoad() {
        relocate(">taboolib.", "com.skillw.pouvoir.taboolib.")
    }

    override fun register(key: String, value: PouScriptEngine) {
        super.register(key, value)
        for (suffix in value.suffixes) {
            suffixMap[suffix] = value
        }
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
}