package com.skillw.pouvoir.internal.script

import com.skillw.pouvoir.internal.manager.PouvoirConfig

object PouClassFilter {
    fun exposeToScripts(className: String): Boolean {
        return !PouvoirConfig["script"].getStringList("banned-packages").stream().anyMatch {
            className.contains(it)
        }
    }
}

