package com.skillw.asahi.internal.context

import com.skillw.asahi.api.annotation.AsahiGetter
import com.skillw.asahi.api.annotation.AsahiSetter
import com.skillw.asahi.api.member.context.AsahiContext
import com.skillw.asahi.api.member.quest.LazyQuester

/**
 * @className Operate
 *
 * @author Glom
 * @date 2023/1/23 15:12 Copyright 2023 user. All rights reserved.
 */

@AsahiSetter
object DefaultSetter : AsahiContext.Setter("default", 999) {
    override fun AsahiContext.filter(key: String) = true

    override fun AsahiContext.setValue(key: String, value: Any): Any? {
        return if (key.contains(".")) putDeep(key, value) else this[key] = value
    }

}

@AsahiGetter
object DefaultGetter : AsahiContext.Getter("default", 999) {
    override fun AsahiContext.filter(key: String) = true

    override fun AsahiContext.getValue(key: String): Any? {
        return (if (key.contains(".")) getDeep(key) else this[key]).run {
            if (this is LazyQuester<*>) get() else this
        }
    }

}