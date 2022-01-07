package com.skillw.pouvoir.api.manager

import com.skillw.pouvoir.api.able.Keyable
import com.skillw.pouvoir.api.able.Pluginable
import com.skillw.pouvoir.api.handle.PManagerHandle
import com.skillw.pouvoir.api.map.KeyMap
import com.skillw.pouvoir.api.plugin.SubPouvoir
import org.bukkit.plugin.java.JavaPlugin

class ManagerData(val pouvoir: SubPouvoir) : KeyMap<String, Manager>(), Pluginable, Keyable<SubPouvoir> {

    private val managers = ArrayList<Manager>()
    override val plugin: JavaPlugin = pouvoir.plugin
    override val key: SubPouvoir = pouvoir

    override fun register(key: String, value: Manager) {
        super.register(key, value)
        managers.add(value)
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

    fun enable() {
        managers.forEach { it.enable() }
    }

    fun active() {
        managers.forEach { it.active() }
    }

    fun load() {
        managers.forEach { it.load() }
    }
}