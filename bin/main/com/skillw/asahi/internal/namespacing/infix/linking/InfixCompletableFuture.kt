package com.skillw.asahi.internal.namespacing.infix.linking

import com.skillw.asahi.api.annotation.AsahiInfix
import com.skillw.asahi.api.member.parser.infix.namespacing.BaseInfix
import java.util.concurrent.CompletableFuture

/**
 * @className ActionCompletableFuture
 *
 * @author Glom
 * @date 2023年1月16日23点47分 Copyright 2023 user. All rights reserved.
 */
@AsahiInfix
internal object InfixCompletableFuture : BaseInfix<CompletableFuture<*>>(CompletableFuture::class.java) {
    init {
        infix("join") { future ->
            future.join()
        }
    }
}