package com.skillw.pouvoir.api.function

import com.skillw.pouvoir.Pouvoir.scriptManager

class PouScriptFunction(key: String, val path: String) : PouFunction(key) {
    override fun predicate(args: Array<String>) = true

    override fun function(args: Array<String>): Any? {
        return scriptManager
            .invokePathWithFunction(
                pathWithFunction = path,
                argsMap = hashMapOf(
                    "args" to args
                )
            )
    }
}