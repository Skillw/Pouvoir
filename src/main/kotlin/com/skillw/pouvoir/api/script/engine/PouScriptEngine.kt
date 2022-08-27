package com.skillw.pouvoir.api.script.engine

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.able.Registrable
import com.skillw.pouvoir.internal.core.script.common.hook.ScriptBridge
import java.util.regex.Pattern
import javax.script.ScriptEngine


/**
 * Pou script engine
 *
 * @constructor Create empty Pou script engine
 */
abstract class PouScriptEngine : Registrable<String> {
    /** Suffixes */
    abstract val suffixes: Array<String>

    /** Function pattern */
    abstract val functionPattern: Pattern

    /** Engine */
    abstract val engine: ScriptEngine

    /** Bridge */
    abstract val bridge: ScriptBridge


    /**
     * Get annotation pattern
     *
     * @return annotation pattern
     */
    open fun getAnnotationPattern(): Pattern = Pattern.compile("//@(?<key>.*)\\((?<args>.*)\\)", 2)

    override fun register() {
        Pouvoir.scriptEngineManager.register(this)
    }

}