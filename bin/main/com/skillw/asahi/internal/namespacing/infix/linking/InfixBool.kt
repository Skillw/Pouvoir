package com.skillw.asahi.internal.namespacing.infix.linking

import com.skillw.asahi.api.annotation.AsahiInfix
import com.skillw.asahi.api.member.parser.infix.namespacing.BaseInfix

/**
 * @className ActionBool
 *
 * @author Glom
 * @date 2022年12月13日14点47分 Copyright 2022 user. All rights reserved.
 */
@AsahiInfix
internal object InfixBool : BaseInfix<Boolean>(Boolean::class.java) {
    init {
        infix("?") { bool ->
            if (bool) {
                val result = parse<Any>()
                expect(":")
                skip()
                result
            } else {
                skip()
                expect(":")
                parse<Any>()
            }
        }
    }
}