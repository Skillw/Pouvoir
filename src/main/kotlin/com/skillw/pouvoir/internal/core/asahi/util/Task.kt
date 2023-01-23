package com.skillw.pouvoir.internal.core.asahi.util

import com.skillw.asahi.api.member.context.AsahiContext
import taboolib.common.platform.function.isPrimaryThread
import taboolib.common.platform.function.submit
import java.util.concurrent.CompletableFuture

/**
 * @className Task
 *
 * @author Glom
 * @date 2023/1/23 18:45 Copyright 2023 user. All rights reserved.
 */
fun AsahiContext.delay(tick: Long) {
    val future = CompletableFuture<Unit>()
    val task = submit(delay = tick, async = !isPrimaryThread) {
        future.complete(null)
    }
    onExit { task.cancel() }
    future.join()
}