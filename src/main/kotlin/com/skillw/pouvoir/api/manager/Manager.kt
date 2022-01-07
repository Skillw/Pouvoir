package com.skillw.pouvoir.api.manager

import com.skillw.pouvoir.api.able.Keyable

interface Manager : Keyable<String>, Comparable<Manager> {
    val priority: Int
    fun init() {}
    fun load() {}
    fun enable() {}
    fun active() {
    }

    fun reload() {}
    fun disable() {}
    override fun register() {}
    fun register(managerData: ManagerData) {
        managerData.register(key, this)
    }

    override fun compareTo(other: Manager): Int {
        return if (priority <= other.priority) {
            -1
        } else 1
    }


}