package com.skillw.pouvoir.api.plugin.map

/**
 * Single exec map
 *
 * @constructor Create empty Single exec map
 */
open class SingleExecMap : LowerMap<() -> Unit>() {
    /** Invoke */
    fun invoke() {
        values.forEach { function -> function.invoke() }
    }

    /**
     * Run
     *
     * @param thing
     */
    open fun run(thing: String) {
        this[thing]?.run { invoke() }
    }
}