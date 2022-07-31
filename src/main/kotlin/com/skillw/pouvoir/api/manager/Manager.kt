package com.skillw.pouvoir.api.manager

import com.skillw.pouvoir.api.able.Registrable
import com.skillw.pouvoir.api.event.ManagerTime
import com.skillw.pouvoir.api.map.BaseMap
import com.skillw.pouvoir.api.map.MultiExecMap
import com.skillw.pouvoir.api.plugin.SubPouvoir
import com.skillw.pouvoir.util.MapUtils.put

interface Manager : Registrable<String>, Comparable<Manager> {
    val priority: Int
    val subPouvoir: SubPouvoir

    fun onLoad() {
    }

    fun onEnable() {}
    fun onActive() {}

    fun onReload() {}
    fun onDisable() {}

    override fun register() {}

    override fun compareTo(other: Manager): Int {
        return if (priority == other.priority) 0
        else if (priority > other.priority) 1
        else -1
    }


    companion object {
        val execMap = BaseMap<Manager, MultiExecMap>()
        val sets = BaseMap<Manager, HashSet<String>>()

        @JvmStatic
        fun Manager.addExec(key: String, managerTime: ManagerTime, exec: () -> Unit) {
            if (sets[this]?.contains(key) == true) {
                return
            }
            sets.put(this, key)
            if (!execMap.containsKey(this)) {
                execMap.put(this, MultiExecMap())
            }
            execMap[this]?.put(managerTime.key, exec)
        }

        @JvmStatic
        fun Manager.call(managerTime: ManagerTime) {
            execMap[this]?.get(managerTime.key)?.forEach { it() }
        }

        @JvmStatic
        fun Manager.removeExec(key: String, managerTime: ManagerTime) {
            sets[this]?.remove(key)
            execMap[this]?.remove(managerTime.key)
        }
    }

}