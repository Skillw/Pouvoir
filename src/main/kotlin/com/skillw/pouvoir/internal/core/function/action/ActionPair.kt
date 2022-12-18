package com.skillw.pouvoir.internal.core.function.action

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.action.PouAction

/**
 * @className ActionMap
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AutoRegister
object ActionPair : PouAction<Pair<*, *>>(Pair::class.java) {
    init {
        addExec("key") { it.first }
        addExec("value") { it.second }
    }

}