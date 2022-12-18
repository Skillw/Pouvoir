package com.skillw.pouvoir.internal.core.function.action

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.action.PouAction

/**
 * @className ActionBool
 *
 * ActionPlayer
 *
 * @author Glom
 * @date 2022年12月13日14点47分 Copyright 2022 user. All rights reserved.
 */
@AutoRegister
object ActionBool : PouAction<Boolean>(Boolean::class.java) {
    init {
        addExec("?") { bool ->
            fun parseValue() {
                if (peek() == "{") parseBlock().execute(this) else parseAny()
            }

            fun skipFalse() {
                except(":")
                if (peek() == "{") parseBlock() else parseAny()
            }
            if (bool)
                parseValue().also { skipFalse() }
            else {
                skipTill("?", ":")
                parseValue()
            }
        }
    }
}