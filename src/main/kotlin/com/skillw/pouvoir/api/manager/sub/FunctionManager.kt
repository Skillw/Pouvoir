package com.skillw.pouvoir.api.manager.sub

import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.map.KeyMap


abstract class FunctionManager : KeyMap<String, PouFunction>(), Manager {
    override val priority = 1
    abstract fun analysis(text: String): String
}