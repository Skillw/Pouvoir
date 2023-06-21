package com.skillw.pouvoir.api.plugin.map

/**
 * Multi exec map
 *
 * @constructor Create empty Multi exec map
 */
open class MultiExecMap : LowerMap<SingleExecMap>() {
    /**
     * Run
     *
     * @param thing
     */
    fun run(key: String) {
        map.filter { it.key == key.lowercase() }.forEach { it.value.invoke() }
    }
}