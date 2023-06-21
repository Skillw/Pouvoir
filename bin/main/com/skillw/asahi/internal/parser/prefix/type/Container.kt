package com.skillw.asahi.internal.parser.prefix.type

import com.skillw.asahi.api.annotation.AsahiTypeParser
import com.skillw.asahi.api.member.lexer.AsahiLexer
import com.skillw.asahi.api.member.quest.Quester
import com.skillw.asahi.api.quest
import com.skillw.asahi.api.questSafely
import com.skillw.asahi.api.quester
import com.skillw.asahi.api.typeParser
import java.util.*

/**
 * @className Collection
 *
 * @author Glom
 * @date 2023/1/16 19:43 Copyright 2023 user. All rights reserved.
 */
/**
 * @className Basic
 *
 * @author Glom
 * @date 2023/1/13 20:22 Copyright 2023 user. All rights reserved.
 */

@AsahiTypeParser
private fun array() = typeParser(Array::class.java) {
    val list = parseList()
    quester {
        list.get().toTypedArray()
    }
}

@AsahiTypeParser
private fun set() = typeParser(Set::class.java, MutableSet::class.java) {
    val list = parseList()
    quester {
        list.get().toHashSet()
    }
}

@AsahiTypeParser
private fun list() = typeParser(List::class.java, MutableList::class.java) {
    val list = parseList()
    quester {
        list.get()
    }
}

private fun AsahiLexer.parseList(): Quester<MutableList<Any?>> {
    this.expect("[", "(")
    val list = LinkedList<Any?>()
    do {
        list += if (this.expect("*")) questSafely<Any?>().quester {
            when (it) {
                is Collection<*>,
                -> list.addAll(it)

                is Array<*>,
                -> list.addAll(it)

                else -> it
            }
        } else questSafely<Any?>()
        this.expect(",")
    } while (!this.expect("]", ")"))
    return quester {
        val result = ArrayList<Any?>()
        list.forEach {
            when (val value = it) {
                is Quester<*> -> result.add(value.get())
                else -> result.add(value)
            }
        }
        result
    }
}

@AsahiTypeParser
private fun map() = typeParser(Map::class.java, MutableMap::class.java) {
    expect("[", "{")
    val list = LinkedList<Pair<Quester<String>, Quester<Any?>>>()
    do {
        val key = quest<String>()
        this.expect("to", "=", ":")
        val value = quest<Any?>()
        list.add(key to value)
        this.expect(",")
    } while (!expect("]", "}"))
    quester {
        val map = LinkedHashMap<String, Any?>()
        list.forEach { (key, value) ->
            map[key.get()] = value.get()
        }
        map
    }
}