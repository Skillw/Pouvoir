package com.skillw.pouvoir.api.map

import java.util.*

open class MultiExecMap : LowerMap<LinkedList<() -> Unit>>() {
    fun invoke() {
        values.forEach { it.forEach { function -> function.invoke() } }
    }

    fun run(thing: String) {
        this[thing]?.run {
            forEach { it.invoke() }
        }
    }
}