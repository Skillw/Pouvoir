package com.skillw.pouvoir.internal.core.function.action

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.action.PouAction

/**
 * @className ActionArray
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AutoRegister
object ActionArray : PouAction<Array<*>>(Array::class.java) {
    init {
        addExec("get") { obj ->
            except("at")
            val index = parseInt()
            return@addExec obj[index]
        }
        addExec("set") { obj ->
            obj as? Array<Any?> ?: error("The obj should be a Array<Any>")
            except("at")
            val index = parseInt()
            except("to")
            val value = parseAny()
            obj[index] = value
            return@addExec value
        }
        addExec("length") { it.size }
    }
}