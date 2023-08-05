package com.skillw.pouvoir.api.manager

import com.skillw.pouvoir.api.plugin.SubPouvoir
import com.skillw.pouvoir.api.plugin.TotalManager
import com.skillw.pouvoir.api.plugin.map.KeyMap
import com.skillw.pouvoir.api.plugin.map.SingleExecMap
import com.skillw.pouvoir.api.plugin.map.component.Registrable
import com.skillw.pouvoir.internal.core.plugin.PouManagerUtils.getPouManagers
import com.skillw.pouvoir.util.safe
import org.bukkit.plugin.java.JavaPlugin
import taboolib.common.platform.function.submit
import java.util.*

class ManagerData(val subPouvoir: SubPouvoir) : KeyMap<String, Manager>(), Registrable<SubPouvoir> {
    private val managers = ArrayList<Manager>()
    val plugin: JavaPlugin = subPouvoir.plugin
    override val key: SubPouvoir = subPouvoir

    override fun put(key: String, value: Manager): Manager? {
        managers.add(value)
        managers.sort()
        return super.put(key, value)
    }

    init {
        for (manager in subPouvoir.getPouManagers()) {
            this.register(manager)
        }
        val dataField = subPouvoir.javaClass.getField("managerData")
        dataField.set(subPouvoir, this)
    }

    override fun register() {
        TotalManager.register(subPouvoir, this)
    }

    fun load() {
        managers.forEach {
            safe(it::onLoad)
        }
    }

    fun enable() {
        managers.forEach {
            safe(it::onEnable)
        }
    }

    fun active() {
        managers.forEach {
            safe(it::onActive)
        }
    }

    private var onReload = SingleExecMap()
    fun reload() {
        submit(async = true) {
            managers.forEach {
                safe(it::onReload)
            }
            onReload.values.forEach { it() }
        }
    }

    fun onReload(key: String = UUID.randomUUID().toString(), exec: () -> Unit) {
        onReload[key] = exec
    }

    fun disable() {
        managers.forEach {
            safe(it::onDisable)
        }
    }

}