package com.skillw.pouvoir.internal.core.asahi.infix.bukkit

import com.skillw.asahi.api.annotation.AsahiInfix
import com.skillw.asahi.api.member.parser.infix.namespacing.BaseInfix
import org.bukkit.event.Cancellable

/**
 * @className ActionCancellable
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AsahiInfix
object InfixCancellable : BaseInfix<Cancellable>(Cancellable::class.java, "bukkit") {
    init {
        "isCancelled" to {
            if (expect("to")) {
                it.isCancelled = parse()
            }
            it.isCancelled
        }
    }
}