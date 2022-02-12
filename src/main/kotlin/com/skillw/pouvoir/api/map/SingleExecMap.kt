package com.skillw.pouvoir.api.map

open class SingleExecMap : LowerMap<() -> Unit>() {
    fun invoke() {
        values.forEach { function -> function.invoke() }
    }

    open fun run(thing: String) {
        this[thing]?.run { invoke() }
    }
}