package com.skillw.pouvoir.api.plugin.map

/**
 * Multi exec map
 *
 * @constructor Create empty Multi exec map
 */
open class MultiMap<K : Any, V> : BaseMap<K, List<V>>() {
    fun add(key: K, value: V): Boolean = (computeIfAbsent(key) { ArrayList() } as MutableList).add(value)
    fun remove(key: K, value: V): Boolean = (get(key) as MutableList?)?.remove(value) ?: false
    operator fun plusAssign(map: MultiMap<K, V>) {
        addAll(map)
    }

    operator fun plusAssign(pair: Pair<K, V>) {
        add(pair.first, pair.second)
    }

    fun addAll(map: MultiMap<K, V>) {
        map.keys.forEach { key ->
            map[key]?.forEach { value ->
                add(key, value)
            }
        }
    }
}