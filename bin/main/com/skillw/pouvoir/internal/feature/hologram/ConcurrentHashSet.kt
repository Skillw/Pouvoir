package com.skillw.pouvoir.internal.feature.hologram

import java.io.Serializable
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * @className ConcrrentSet
 *
 * @author Glom
 * @date 2022/7/28 11:37 Copyright 2022 user. All rights reserved.
 */
class ConcurrentHashSet<E> : AbstractSet<E>(), Serializable {
    private val map = ConcurrentHashMap<E, Boolean?>()

    override operator fun contains(element: E): Boolean {
        return map.containsKey(element)
    }

    override fun add(element: E): Boolean {
        return map.putIfAbsent(element, java.lang.Boolean.TRUE) == null
    }

    override fun remove(element: E): Boolean {
        return map.remove(element ?: return false) != null
    }

    override fun clear() {
        map.clear()
    }

    override fun iterator(): MutableIterator<E> {
        return map.keys.iterator()
    }

    companion object {
        private const val serialVersionUID = -6761513279741915432L
    }

    override val size: Int
        get() = map.size
}
