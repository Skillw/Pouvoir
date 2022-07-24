package com.skillw.pouvoir.internal.function

import com.skillw.pouvoir.Pouvoir.scriptManager
import com.skillw.pouvoir.api.function.PouFunction

class PouScriptFunction(key: String, val path: String) : PouFunction(key) {
    override fun predicate(args: Array<String>) = true

    override fun function(args: Array<String>): Any? {
        return scriptManager
            .invoke(
                path,
                variables = hashMapOf(
                    "args" to args
                )
            )
    }
}