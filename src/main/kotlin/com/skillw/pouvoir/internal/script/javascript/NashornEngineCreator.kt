package com.skillw.pouvoir.internal.script.javascript

import javax.script.ScriptEngine

fun interface NashornEngineCreator {
    fun getEngine(vararg args: String): ScriptEngine
}