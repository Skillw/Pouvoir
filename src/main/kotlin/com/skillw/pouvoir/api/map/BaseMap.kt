package com.skillw.pouvoir.api.map

import com.skillw.pouvoir.api.able.RegContainer
import java.util.concurrent.ConcurrentHashMap


open class BaseMap<K : Any, V : Any> : RegContainer<K, V> {
    val map = ConcurrentHashMap<K, V>()
    override fun register(key: K, value: V) {
        put(key, value)
    }


    override val size: Int
        get() = map.size
    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
        get() = map.entries
    override val keys: MutableSet<K>
        get() = map.keys
    override val values: MutableCollection<V>
        get() = map.values

    override fun clear() {
        map.clear()
    }

    override fun isEmpty(): Boolean {
        return map.isEmpty()
    }

    inline fun forEach(action: (Map.Entry<K, V>) -> Unit) {
        for (element in map) action(element)
    }

    override operator fun get(key: K): V? {
        return map[key]
    }

    override fun containsValue(value: V): Boolean {
        return map.containsValue(value)
    }

    fun putAll(baseMap: BaseMap<K, out V>) {
        map.putAll(baseMap.map)
    }

    override fun containsKey(key: K): Boolean {
        return map.containsKey(key)
    }

    override fun remove(key: K): V? {
        return map.remove(key)
    }

    override fun putAll(from: Map<out K, V>) {
        map.putAll(from)
    }

    override fun put(key: K, value: V): V? {
        return map.put(key, value)
    }

    fun isNotEmpty(): Boolean = !isEmpty()

    operator fun contains(key: K): Boolean =
        containsKey(key)


    open operator fun set(key: K, value: V) {
        put(key, value)
    }


    inline fun getOrElse(key: K, defaultValue: () -> V): V = get(key) ?: defaultValue()


    internal inline fun getOrElseNullable(key: K, defaultValue: () -> V): V {
        val value = get(key)
        if (value == null && !containsKey(key)) {
            return defaultValue()
        } else {
            @Suppress("UNCHECKED_CAST")
            return value as V
        }
    }


    inline fun getOrPut(key: K, defaultValue: () -> V): V {
        val value = get(key)
        return if (value == null) {
            val answer = defaultValue()
            put(key, answer)
            answer
        } else {
            value
        }
    }


    operator fun iterator(): Iterator<Map.Entry<K, V>> = entries.iterator()


    inline fun <R, M : MutableMap<in K, in R>> mapValuesTo(
        destination: M,
        transform: (MutableMap.MutableEntry<K, V>) -> R,
    ): M {
        return entries.associateByTo(destination, { it.key }, transform)
    }


    inline fun <R, M : MutableMap<R, V>> mapKeysTo(
        destination: M,
        transform: (MutableMap.MutableEntry<K, V>) -> R,
    ): M {
        return entries.associateByTo(destination, transform) { it.value }
    }


    fun putAll(pairs: Array<out Pair<K, V>>) {
        for ((key, value) in pairs) {
            put(key, value)
        }
    }


    inline fun <R> mapValues(transform: (Map.Entry<K, V>) -> R): Map<K, R> {
        return mapValuesTo(LinkedHashMap(size), transform) // .optimizeReadOnlyMap()
    }

    inline fun <R : Any> mapKeys(transform: (Map.Entry<K, V>) -> R): Map<R, V> {
        return mapKeysTo(LinkedHashMap(size), transform) // .optimizeReadOnlyMap()
    }


    inline fun filterKeys(predicate: (K) -> Boolean): Map<K, V> {
        val result = LinkedHashMap<K, V>()
        for (entry in map) {
            if (predicate(entry.key)) {
                result[entry.key] = entry.value
            }
        }
        return result
    }

    inline fun filterValues(predicate: (V) -> Boolean): Map<K, V> {
        val result = LinkedHashMap<K, V>()
        for (entry in map) {
            if (predicate(entry.value)) {
                result[entry.key] = entry.value
            }
        }
        return result
    }


    operator fun plus(pair: Pair<K, V>): BaseMap<K, V> {
        this.map += pair
        return this
    }


    operator fun plus(pairs: Array<out Pair<K, V>>): BaseMap<K, V> {
        this.map.putAll(pairs)
        return this
    }


    operator fun plus(map: Map<out K, V>): BaseMap<K, V> {
        this.map.putAll(map)
        return this
    }


    operator fun plusAssign(pair: Pair<K, V>) {
        this.map[pair.first] = pair.second
    }


    operator fun plusAssign(pairs: Iterable<Pair<K, V>>) {
        this.map.putAll(pairs)
    }


    operator fun plusAssign(pairs: Array<out Pair<K, V>>) {
        this.map.putAll(pairs)
    }


    operator fun plusAssign(pairs: Sequence<Pair<K, V>>) {
        this.map.putAll(pairs)
    }


    operator fun plusAssign(map: Map<out K, V>) {
        this.map.putAll(map)
    }

    inline fun filter(predicate: (Map.Entry<K, V>) -> Boolean): Map<K, V> {
        return map.filterTo(LinkedHashMap(), predicate)
    }

    inline fun <R, C : MutableCollection<in R>> flatMapTo(
        destination: C,
        transform: (Map.Entry<K, V>) -> Sequence<R>,
    ): C {
        for (element in this) {
            val list = transform(element)
            destination.addAll(list)
        }
        return destination
    }

    inline fun <R> map(transform: (Map.Entry<K, V>) -> R): List<R> {
        return map.mapTo(ArrayList(size), transform)
    }

    inline fun <R : Any> mapNotNull(transform: (Map.Entry<K, V>) -> R?): List<R> {
        return mapNotNullTo(ArrayList(), transform)
    }

    inline fun <R : Any, C : MutableCollection<in R>> mapNotNullTo(
        destination: C,
        transform: (Map.Entry<K, V>) -> R?,
    ): C {
        forEach { element -> transform(element)?.let { destination.add(it) } }
        return destination
    }

    inline fun any(predicate: (Map.Entry<K, V>) -> Boolean): Boolean {
        if (isEmpty()) return false
        for (element in this) if (predicate(element)) return true
        return false
    }
}
