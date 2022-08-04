package com.skillw.pouvoir.internal.function.common

import com.skillw.pouvoir.Pouvoir.pouFunctionManager
import com.skillw.pouvoir.api.able.Keyable
import com.skillw.pouvoir.internal.function.reader.SimpleReader

/**
 * @className PouFunc
 *
 * @author Glom
 * @date 2022/8/1 9:49 Copyright 2022 user. All rights reserved.
 */
class PouFunc(override val key: String, private val params: Array<String>, content: String) : Keyable<String> {
    private val reader = SimpleReader(content)

    fun execute(vararg args: Any): Any? {
        return pouFunctionManager.parse(reader) {
            params.forEachIndexed { index, paramName ->
                this[paramName] = args[index]
            }
        }
    }

}