package com.skillw.pouvoir.api.manager.sub.script

import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.map.KeyMap
import com.skillw.pouvoir.api.script.engine.PouScriptEngine

abstract class ScriptEngineManager : KeyMap<String, PouScriptEngine>(), Manager {
    abstract fun getEngine(suffix: String): PouScriptEngine?
}

