package com.skillw.pouvoir.api.map

import java.util.*

/**
 * Multi exec map
 *
 * @constructor Create empty Multi exec map
 */
open class MultiExecMap : LowerMap<LinkedList<() -> Unit>>() {
    /** Invoke */
    fun invoke() {
        values.forEach { it.forEach { function -> function.invoke() } }
    }

    /**
     * Run
     *
     * @param thing
     */
    fun run(thing: String) {
        map.filter { it.key.startsWith(thing.lowercase()) }.forEach { it.value.forEach { exec -> exec.invoke() } }
    }
}