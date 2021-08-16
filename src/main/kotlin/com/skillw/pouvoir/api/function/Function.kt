package com.skillw.pouvoir.api.function

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.Pouvoir.scriptManager
import com.skillw.pouvoir.api.able.Keyable
import java.util.function.Function

class Function(override val key: String, private val func: (Array<String>) -> Any?) : Keyable<String>,
    Function<Array<String>, Any?> {

    constructor(key: String, path: String) : this(key,
        {
            scriptManager
                .invokePathWithFunction(
                    path,
                    hashMapOf(
                        Pair("args", it),
                    )
                )
        })

    override fun register() {
        Pouvoir.functionManager.register(this)
    }

    override fun apply(args: Array<String>): Any? {
        return func.invoke(args)
    }

}