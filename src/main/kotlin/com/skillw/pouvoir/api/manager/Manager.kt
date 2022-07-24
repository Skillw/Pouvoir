package com.skillw.pouvoir.api.manager

import com.skillw.pouvoir.api.able.Registrable
import com.skillw.pouvoir.api.event.ManagerTime
import com.skillw.pouvoir.api.event.PouManagerEvent
import com.skillw.pouvoir.api.map.BaseMap
import com.skillw.pouvoir.api.plugin.SubPouvoir
import com.skillw.pouvoir.util.MapUtils.put
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.function.registerBukkitListener

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
        val sets = BaseMap<ManagerTime, HashSet<String>>()

        @JvmStatic
        fun Manager.addExec(key: String, managerTime: ManagerTime, exec: () -> Unit) {
            if (sets[managerTime]?.contains(key) == true) {
                return
            }
            sets.put(managerTime, key)
            registerBukkitListener(PouManagerEvent::class.java, EventPriority.HIGHEST, false) {
                if (it.manager == this@addExec && it.time == managerTime) exec.invoke()
            }
        }
    }

}