package com.skillw.pouvoir.internal.core.function.action

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.action.ActionExecutor
import com.skillw.pouvoir.api.function.action.PouAction
import org.bukkit.event.Cancellable

/**
 * @className ActionMap
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AutoRegister
object ActionCancellable : PouAction<Cancellable>(
    Cancellable::class.java,
    "isCancelled" to ActionExecutor {
        if (except("to")) {
            it.isCancelled = parseBoolean()
        }
        return@ActionExecutor it.isCancelled
    })