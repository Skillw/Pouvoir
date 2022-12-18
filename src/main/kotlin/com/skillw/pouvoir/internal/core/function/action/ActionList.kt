package com.skillw.pouvoir.internal.core.function.action

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.action.PouAction

/**
 * @className ActionList
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AutoRegister
object ActionList : PouAction<MutableList<*>>(MutableList::class.java) {

    init {
        addExec("get") { obj ->
            except("at")
            val index = parseInt()
            obj[index]
        }

        addExec("add") { obj ->
            obj as? MutableList<Any?>? ?: error("MutableList")
            val value = parseAny()
            obj.add(value)
        }

        addExec("remove") { obj ->
            if (except("at")) {
                return@addExec obj.removeAt(parseInt())
            }
            val value = parseAny()
            obj.remove(value)
        }

        addExec("clear") { obj ->
            obj.clear()
        }

        addExec("set") { obj ->
            obj as? MutableList<Any?>? ?: error("MutableList")
            val index = parseInt()
            val value = parseAny()
            obj[index] = value
            return@addExec value
        }

        addExec("size") { obj ->
            obj.size
        }

        addExec("contains") { obj ->
            val value = parseAny()
            obj.contains(value)
        }

        addExec("isEmpty") { obj ->
            obj.isEmpty()
        }

        addExec("toArray") { obj ->
            obj.toTypedArray()
        }

        addExec("toString") { obj ->
            obj.toString()
        }

        addExec("merge") { obj ->
            except("by")
            val by = parseString()
            obj.joinToString { by.replace("\\n", "\n") }
        }
    }
}