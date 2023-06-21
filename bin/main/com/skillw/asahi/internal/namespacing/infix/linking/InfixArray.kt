package com.skillw.asahi.internal.namespacing.infix.linking

import com.skillw.asahi.api.annotation.AsahiInfix
import com.skillw.asahi.api.member.parser.infix.namespacing.BaseInfix

/**
 * @className ActionArray
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AsahiInfix
internal object InfixArray : BaseInfix<Array<*>>(Array::class.java) {
    init {
        //添加后缀动作        这个obj是当前操作的对象
        infix("get") { obj ->
            //预期下一个Token是 "at"
            expect("at")
            //          获取一个Int
            val index = parse<Int>()
            //返回结果
            return@infix obj[index]
        }

        infix("set") { obj ->
            obj as? Array<Any?> ?: error("The tokenizer should be a Array<Any>")
            expect("at")
            val index = parse<Int>()
            expect("to")
            val value = parse<Any>()
            obj[index] = value
            return@infix value
        }
        infix("length") { it.size }
    }
}