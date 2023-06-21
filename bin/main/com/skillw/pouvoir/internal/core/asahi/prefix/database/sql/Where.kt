package com.skillw.pouvoir.internal.core.asahi.prefix.database.sql

import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.member.context.AsahiContext
import com.skillw.asahi.api.prefixParser
import com.skillw.asahi.api.quest
import taboolib.module.database.WhereData
import taboolib.module.database.WhereExecutor

private fun AsahiContext.where(): WhereExecutor {
    return get("@where") as WhereExecutor
}

private fun Any.formatColumn(): String {
    var escape = false
    var exists = false
    val join = toString().toCharArray().joinToString("") {
        when (it) {
            '`' -> {
                exists = true
                it.toString()
            }

            '\\' -> {
                if (escape) {
                    "\\"
                } else {
                    escape = true
                    ""
                }
            }

            '.' -> {
                if (escape) {
                    it.toString()
                } else {
                    "`.`"
                }
            }

            else -> it.toString()
        }
    }
    return if (exists) join else "`$join`"
}

@AsahiPrefix(["where"], "sql-where-action")
private fun where() = prefixParser {
    val keyGetter = quest<String>()
    val symbolGetter = quest<String>()
    val valueGetter = quest<String>()
    result {
        val key = keyGetter.get()
        val symbol = symbolGetter.get().let { if (it == "==") "=" else it }
        val value = valueGetter.get()
        where().run {
            WhereData("${key.formatColumn()} $symbol ?", listOf(value)).also {
                append(it)
            }
        }
    }
}

@AsahiPrefix(["and"], "sql-where-action")
private fun and() = prefixParser {
    val dataGetter = quest<WhereData>()
    result {
        val data = dataGetter.get()
        where().and {
            append(data)
        }
    }
}

@AsahiPrefix(["or"], "sql-where-action")
private fun or() = prefixParser {
    val dataGetter = quest<WhereData>()
    result {
        val data = dataGetter.get()
        where().or {
            append(data)
        }
    }
}

@AsahiPrefix(["not"], "sql-where-action")
private fun not() = prefixParser {
    val dataGetter = quest<WhereData>()
    result {
        val data = dataGetter.get()
        where().not(data)
    }
}