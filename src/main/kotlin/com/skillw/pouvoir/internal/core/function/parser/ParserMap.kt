package com.skillw.pouvoir.internal.core.function.parser

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.parser.Parser
import com.skillw.pouvoir.api.function.parser.TokenParser

@AutoRegister
object ParserMap : TokenParser(Map::class.java, MutableMap::class.java) {
    override fun parse(parser: Parser): Any? {
        with(parser) {
            if (except("[")) {
                val map = LinkedHashMap<String, Any>()
                while (hasNext()) {
                    val key = parseString()
                    except("to", "=", ":")
                    val value = parse<Any>()
                    map[key] = value
                    except(",")
                    if (except("]")) break
                }
                return map
            }
            return null
        }
    }
}