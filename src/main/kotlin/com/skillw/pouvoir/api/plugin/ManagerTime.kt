package com.skillw.pouvoir.api.plugin

/**
 * @className ManagerTime
 *
 * @author Glom
 * @date 2022/7/22 16:11 Copyright 2022 user. All rights reserved.
 */
class ManagerTime(val key: String) {
    companion object {
        val BEFORE_LOAD = ManagerTime("BeforeLoad")
        val LOAD = ManagerTime("Load")
        val BEFORE_ENABLE = ManagerTime("BeforeEnable")
        val ENABLE = ManagerTime("Enable")
        val BEFORE_ACTIVE = ManagerTime("BeforeActive")
        val ACTIVE = ManagerTime("Active")
        val BEFORE_RELOAD = ManagerTime("BeforeReload")
        val RELOAD = ManagerTime("Reload")
        val BEFORE_DISABLE = ManagerTime("BeforeDisable")
        val DISABLE = ManagerTime("Disable")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ManagerTime) return false
        return other.key.equals(key, true)
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }


}