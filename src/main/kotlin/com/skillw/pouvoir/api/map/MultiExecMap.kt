package com.skillw.pouvoir.api.map

import java.util.*

open class MultiExecMap : LowerMap<LinkedList<() -> Unit>>() {
    fun invoke() {
        values.forEach { it.forEach { function -> function.invoke() } }
    }

    fun run(thing: String) {
        map.filter { it.key.startsWith(thing.lowercase()) }.forEach { it.value.forEach { exec -> exec.invoke() } }
    }
}