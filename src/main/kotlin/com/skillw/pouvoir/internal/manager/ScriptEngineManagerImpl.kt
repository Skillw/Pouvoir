package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.manager.sub.script.ScriptEngineManager
import com.skillw.pouvoir.api.plugin.TotalManager
import com.skillw.pouvoir.api.script.engine.PouScriptEngine
import com.skillw.pouvoir.internal.handle.PouEngineHandle
import java.util.concurrent.ConcurrentHashMap

object ScriptEngineManagerImpl : ScriptEngineManager() {
    override val key = "ScriptEngineManager"
    override val priority: Int = 5
    override val subPouvoir = Pouvoir


    private val suffixMap = ConcurrentHashMap<String, PouScriptEngine>()

    override fun register(key: String, value: PouScriptEngine) {
        super.register(key, value)
        for (suffix in value.suffixes) {
            suffixMap[suffix] = value
        }
    }

    override fun getEngine(suffix: String): PouScriptEngine? {
        return suffixMap[suffix]
    }

    override fun onInit() {
        TotalManager.forEachClass {
            PouEngineHandle.inject(it)
        }
    }
}