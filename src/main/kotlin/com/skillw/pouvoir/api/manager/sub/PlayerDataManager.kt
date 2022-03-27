package com.skillw.pouvoir.api.manager.sub

import com.skillw.pouvoir.api.manager.Manager

abstract class PlayerDataManager : Manager {
    abstract operator fun get(user: String, key: String): String?
    abstract fun delete(user: String, key: String)
    abstract operator fun set(user: String, key: String, value: String)
}