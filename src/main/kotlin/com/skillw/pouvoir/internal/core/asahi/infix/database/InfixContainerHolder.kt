package com.skillw.pouvoir.internal.core.asahi.infix.database

import com.skillw.asahi.api.annotation.AsahiInfix
import com.skillw.asahi.api.member.parser.infix.namespacing.BaseInfix
import com.skillw.pouvoir.api.feature.database.ContainerHolder

/**
 * @className ActionCancellable
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. 
 */
@AsahiInfix
object InfixContainerHolder : BaseInfix<ContainerHolder<*>>(ContainerHolder::class.java) {
    init {
        "container" to {
            val key = parseString()
            val user = !expect("normal")
            it.container(key, user)
        }
    }
}