package com.skillw.asahi.internal.namespacing.infix.linking

import com.skillw.asahi.api.annotation.AsahiInfix
import com.skillw.asahi.api.member.parser.infix.namespacing.BaseInfix

/**
 * @className ActionPair
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AsahiInfix
internal object InfixPair : BaseInfix<Pair<*, *>>(Pair::class.java) {
    init {
        "key" to { it.first }
        "value" to { it.second }
    }
}