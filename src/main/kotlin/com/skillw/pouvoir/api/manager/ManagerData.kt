package com.skillw.pouvoir.api.manager

import com.skillw.pouvoir.api.able.Keyable
import com.skillw.pouvoir.api.able.Pluginable
import com.skillw.pouvoir.api.handle.PManagerHandle
import com.skillw.pouvoir.api.map.KeyMap
import com.skillw.pouvoir.api.plugin.SubPouvoir
import taboolib.platform.BukkitPlugin

class ManagerData(val pouvoir: SubPouvoir) : KeyMap<String, Manager>(), Pluginable, Keyable<SubPouvoir> {

    override val plugin: BukkitPlugin = pouvoir.plugin
    override val key: SubPouvoir = pouvoir

    init {
        for (manager in PManagerHandle.getPManagers(pouvoir)) {
            manager.register(this)
        }
        val dataField = pouvoir.javaClass.getField("managerData")
        dataField.set(pouvoir, this)
    }

    override fun register() {
        map.values.forEach { it.init() }
        TotalManager.register(pouvoir, this)
    }

    fun reload() {
        map.values.forEach {
            it.reload()
        }
    }

    fun disable() {
        map.values.forEach { it.disable() }
    }
}