package com.skillw.pouvoir.internal.core.asahi.prefix.database.sql

import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.prefixParser
import com.skillw.asahi.api.quest
import com.skillw.asahi.api.quester
import com.skillw.asahi.api.script.linking.NativeFunction
import com.skillw.pouvoir.api.feature.database.sql.IPouTable
import taboolib.module.database.QueryTask

/**
 * @className Sql
 *
 * @author Glom
 * @date 2023/1/19 15:05 Copyright 2023 user. All rights reserved.
 */

@AsahiPrefix(["sql"], "sql")
private fun sql() = prefixParser {
    val containerGetter = if (expect("of")) quest<IPouTable<*, *>>() else quester { selector() }
    when (val top = next()) {

        "create" -> result {
            containerGetter.get().createTable()
        }

        "select" -> {
            if (expect("all")) {
                return@prefixParser result {
                    containerGetter.get().select { }
                }
            }
            expect("where")
            val where = parseScript("sql-where-action")
            result {
                containerGetter.get().select {
                    where { temp("@where" to this) { where.run() } }
                }
            }
        }

        "find" -> {
            expect("where")
            val where = parseScript("sql-where-action")
            result {
                containerGetter.get().find {
                    where { temp("@where" to this) { where.run() } }
                }
            }
        }

        "update" -> {
            expect("where")
            val where = parseScript("sql-where-action")
            expect("to")
            val data = questMap()
            result {
                containerGetter.get().update {
                    where { temp("@where" to this) { where.run() } }
                    data.get().forEach { (key, value) ->
                        set(key, value)
                    }
                }
            }
        }

        "delete" -> {
            if (expect("all")) {
                return@prefixParser result {
                    containerGetter.get().delete { }
                }
            }
            val where = parseScript("sql-where-action")
            result {
                containerGetter.get().delete {
                    where { temp("@where" to this) { where.run() } }
                }
            }
        }

        "insert" -> {
            expect("with")
            val keys = quest<List<Any>>()
            expect("to")
            val values = questList()
            result {
                containerGetter.get().insert(*keys.get().map { it.toString() }.toTypedArray()) {
                    value(*values.get().filterNotNull().toTypedArray())
                }
            }
        }

        else -> error("Unknown User Data operate type $top")
    }
}

@AsahiPrefix(["query"], "sql")
private fun query() = prefixParser {
    val queryGetter = if (expect("of")) quest<QueryTask>() else quester { selector() }
    when (val top = next()) {
        "find" -> result { queryGetter.get().find() }

        "first" -> {
            val todo = quest<NativeFunction>()
            result {
                queryGetter.get().first {
                    todo.get().invoke(context(), this)
                }
            }
        }

        "firstOrNull" -> {
            val todo = quest<NativeFunction>()
            result {
                queryGetter.get().firstOrNull {
                    todo.get().invoke(context(), this)
                }
            }
        }

        "map" -> {
            val todo = quest<NativeFunction>()
            result {
                queryGetter.get().map {
                    todo.get().invoke(context(), this)
                }
            }
        }

        "forEach" -> {
            val todo = quest<NativeFunction>()
            result {
                queryGetter.get().forEach {
                    todo.get().invoke(context(), this)
                }
            }
        }


        else -> error("Unknown User Data operate type $top")
    }
}