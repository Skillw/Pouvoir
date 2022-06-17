package com.skillw.pouvoir.api.manager

import com.skillw.pouvoir.api.able.Keyable
import com.skillw.pouvoir.api.map.MultiExecMap
import com.skillw.pouvoir.api.plugin.SubPouvoir
import com.skillw.pouvoir.util.MapUtils.addSingle

interface Manager : Keyable<String>, Comparable<Manager> {

    val priority: Int
    val subPouvoir: SubPouvoir

    fun onInit() {}
    fun onLoad() {
    }

    fun onEnable() {}
    fun onActive() {}

    fun onReload() {}
    fun onDisable() {}

    override fun register() {}
    fun register(managerData: ManagerData) {
        managerData.register(key, this)
    }

    override fun compareTo(other: Manager): Int {
        return if (priority == other.priority) 0
        else if (priority > other.priority) 1
        else -1
    }

    companion object {
        const val BEFORE_INIT = "BeforeInit"
        const val INIT = "Init"
        const val BEFORE_LOAD = "BeforeLoad"
        const val LOAD = "Load"
        const val BEFORE_ENABLE = "BeforeEnable"
        const val ENABLE = "Enable"
        const val BEFORE_ACTIVE = "BeforeActive"
        const val ACTIVE = "Active"
        const val BEFORE_RELOAD = "BeforeReload"
        const val RELOAD = "Reload"
        const val BEFORE_DISABLE = "BeforeDisable"
        const val DISABLE = "Disable"

        @JvmStatic
        fun Manager.run(thing: String) {
            this.subPouvoir.managerData.run(this, thing)
        }

        @JvmStatic
        fun Manager.addSingle(thing: String, exec: () -> Unit) {
            val map = this.subPouvoir.managerData.exec
            if (map.containsKey(this)) {
                map[this]!!.addSingle(thing, exec)
            } else {
                val multiExecMap = MultiExecMap()
                multiExecMap.addSingle(thing, exec)
                map[this] = multiExecMap
            }
        }
    }
}