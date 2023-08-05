package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.manager.sub.script.ScriptEngineManager
import com.skillw.pouvoir.api.script.engine.PouScriptEngine
import com.skillw.pouvoir.util.replacement
import java.util.concurrent.ConcurrentHashMap

internal object ScriptEngineManagerImpl : ScriptEngineManager() {
    override val key = "ScriptEngineManager"
    override val priority: Int = 5
    override val subPouvoir = Pouvoir
    private val suffixMap = ConcurrentHashMap<String, PouScriptEngine>()
    override val globalVariables: MutableMap<String, Any> = ConcurrentHashMap()
    private val relocates = HashMap<String, String>()


    override fun onLoad() {
        relocate(">taboolib.", "com.skillw.pouvoir.taboolib.")
        relocate(">kotlin.", "kotlin1720.")

        values.forEach(PouScriptEngine::onLoad)
    }

    override fun onEnable() {
        values.forEach(PouScriptEngine::onEnable)
    }

    override fun onReload() {
        values.forEach(PouScriptEngine::onReload)
    }

    override fun onDisable() {
        values.forEach(PouScriptEngine::onDisable)
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

    override fun relocatePath(clazzPath: String): String {
        return clazzPath.replacement(relocates)
    }

    override fun deleteRelocate(from: String) {
        relocates.remove(from)
    }
}