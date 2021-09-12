package com.skillw.pouvoir.api.manager

import com.skillw.pouvoir.api.able.Keyable
import com.skillw.pouvoir.api.able.Pluginable
import com.skillw.pouvoir.api.handle.PManagerHandle
import com.skillw.pouvoir.api.map.KeyMap
import com.skillw.pouvoir.api.plugin.SubPouvoir
import taboolib.platform.BukkitPlugin

class ManagerData(val pouvoir: SubPouvoir) : KeyMap<String, Manager>(), Pluginable, Keyable<SubPouvoir> {

    private val managers = ArrayList<Manager>()
    override val plugin: BukkitPlugin = pouvoir.plugin
    override val key: SubPouvoir = pouvoir

    override fun register(key: String, manager: Manager) {
        super.register(key, manager)
        managers.add(manager)
        managers.sort()
    }

    init {
        for (manager in PManagerHandle.getPManagers(pouvoir)) {
            manager.register(this)
        }
        val dataField = pouvoir.javaClass.getField("managerData")
        dataField.set(pouvoir, this)
    }

    override fun register() {
        managers.forEach { it.init() }
        TotalManager.register(pouvoir, this)
    }

    fun reload() {
        managers.forEach {
            it.reload()
        }
    }

    fun disable() {
        managers.forEach { it.disable() }
    }
}