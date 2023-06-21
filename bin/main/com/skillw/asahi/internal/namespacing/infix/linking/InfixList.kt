package com.skillw.asahi.internal.namespacing.infix.linking

import com.skillw.asahi.api.annotation.AsahiInfix
import com.skillw.asahi.api.member.parser.infix.namespacing.BaseInfix

/**
 * @className ActionList
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AsahiInfix
internal object InfixList : BaseInfix<MutableList<*>>(MutableList::class.java) {

    init {
        "get" to { obj ->
            expect("at")
            val index = parse<Int>()
            obj[index]
        }

        infix("add") { obj ->
            obj as? MutableList<Any?>? ?: error("MutableList")
            val value = parse<Any>()
            obj.add(value)
        }

        infix("addAll") { obj ->
            obj as? MutableList<Any?>? ?: error("MutableList")
            val value = parse<List<Any?>>()
            obj.addAll(value)
        }

        infix("remove") { obj ->
            if (expect("at")) {
                return@infix obj.removeAt(parse<Int>())
            }
            val value = parse<Any>()
            obj.remove(value)
        }

        infix("clear") { obj ->
            obj.clear()
        }

        infix("set") { obj ->
            obj as? MutableList<Any?>? ?: error("MutableList")
            val index = parse<Int>()
            val value = parse<Any>()
            obj[index] = value
            return@infix value
        }

        infix("size") { obj ->
            obj.size
        }

        infix("contains") { obj ->
            val value = parse<Any>()
            obj.contains(value)
        }

        infix("isEmpty") { obj ->
            obj.isEmpty()
        }

        infix("toArray") { obj ->
            obj.toTypedArray()
        }

        infix("toString") { obj ->
            obj.toString()
        }

        infix("merge") { obj ->
            expect("by")
            val by = parse<String>()
            obj.joinToString(by.replace("\\n", "\n"))
        }
    }
}