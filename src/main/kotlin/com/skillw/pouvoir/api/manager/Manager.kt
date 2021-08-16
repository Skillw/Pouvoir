package com.skillw.pouvoir.api.manager

import com.skillw.pouvoir.api.able.Keyable

interface Manager : Keyable<String> {
    fun init() {}
    fun reload() {}
    fun disable() {}
    override fun register() {}
    fun register(managerData: ManagerData) {
        managerData.register(key, this)
    }
}