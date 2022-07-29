package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.manager.sub.script.ScriptEngineManager
import com.skillw.pouvoir.api.script.engine.PouScriptEngine
import com.skillw.pouvoir.internal.script.common.top.TopLevel
import com.skillw.pouvoir.util.MessageUtils.information
import taboolib.common.platform.function.info
import taboolib.module.chat.colored
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import javax.script.ScriptContext
import javax.script.ScriptContext.ENGINE_SCOPE

object ScriptEngineManagerImpl : ScriptEngineManager() {
    override val key = "ScriptEngineManager"
    override val priority: Int = 5
    override val subPouvoir = Pouvoir
    private val suffixMap = ConcurrentHashMap<String, PouScriptEngine>()
    override val globalVariables: MutableMap<String, Any> = ConcurrentHashMap()

    internal fun ScriptContext.addCheckVarsFunc() {
        setAttribute(
            "checkUsableVars",
            Runnable {
                val messages = LinkedList<String>()
                getBindings(ENGINE_SCOPE)?.forEach { (key, value) ->
                    messages += value.information(key)
                }
                messages.sort()
                messages.addFirst("&dUsable Variables:")
                messages.forEach {
                    info(it.colored())
                }
                TopLevel.getInfo().forEach {
                    info(it.colored())
                }
            }, ENGINE_SCOPE
        )
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
}