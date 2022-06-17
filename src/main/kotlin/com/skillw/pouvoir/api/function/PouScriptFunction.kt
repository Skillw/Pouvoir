package com.skillw.pouvoir.api.function

import com.skillw.pouvoir.Pouvoir.scriptManager

class PouScriptFunction(key: String, path: String) : PouFunction(key, { true },
    {
        scriptManager
            .invokePathWithFunction(
                pathWithFunction = path,
                argsMap = hashMapOf(
                    "args" to it
                )
            )
    })