package com.skillw.pouvoir.internal.core.function.parser

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.parser.Parser
import com.skillw.pouvoir.api.function.parser.TokenParser
import java.util.*

@AutoRegister
object ListParser :
    TokenParser(List::class.java, MutableList::class.java) {
    override fun parse(parser: Parser): Any? {
        with(parser) {
            if (except("[")) {
                val list = LinkedList<Any>()
                while (hasNext()) {
                    if (except("*")) {
                        when (val collection = parse<Any?>() ?: error("parse Collection / Array error")) {
                            is Collection<*> -> list.addAll(collection.toList().filterNotNull())
                            is Array<*> -> list.addAll(collection.filterNotNull())
                            else -> list.add(collection)
                        }
                    } else list.add(parse())
                    except(",")
                    if (except("]")) break
                }
                return list
            }
            return null
        }
    }
}