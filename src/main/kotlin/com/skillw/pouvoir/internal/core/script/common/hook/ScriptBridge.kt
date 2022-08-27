package com.skillw.pouvoir.internal.core.script.common.hook

import javax.script.CompiledScript
import javax.script.ScriptEngine

interface ScriptBridge {
    fun getEngine(vararg args: String): ScriptEngine
    fun buildInvoker(script: CompiledScript): Invoker
    fun toObject(any: Any): Any?
}