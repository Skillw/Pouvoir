package com.skillw.pouvoir.api.manager

import com.skillw.pouvoir.api.able.Registrable
import com.skillw.pouvoir.api.event.ManagerTime
import com.skillw.pouvoir.api.manager.Manager.Companion.call
import com.skillw.pouvoir.api.map.KeyMap
import com.skillw.pouvoir.api.plugin.SubPouvoir
import com.skillw.pouvoir.api.plugin.TotalManager
import com.skillw.pouvoir.internal.core.plugin.PouManagerUtils.getPouManagers
import org.bukkit.plugin.java.JavaPlugin
import taboolib.common.platform.function.submit

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
        println(map { it.key }.toString())
        val dataField = subPouvoir.javaClass.getField("managerData")
        dataField.set(subPouvoir, this)
    }

    override fun register() {
        TotalManager.register(subPouvoir, this)
    }

    fun load() {
        managers.forEach {
            try {
                it.call(ManagerTime.BEFORE_LOAD)
                it.onLoad()
                it.call(ManagerTime.LOAD)
            } catch (throwable: Throwable) {
                throwable.printStackTrace()

            }
        }
    }

    fun enable() {
        managers.forEach {
            try {
                it.call(ManagerTime.BEFORE_ENABLE)
                it.onEnable()
                it.call(ManagerTime.ENABLE)
            } catch (throwable: Throwable) {
                throwable.printStackTrace()

            }
        }
    }

    fun active() {
        managers.forEach {
            try {
                it.call(ManagerTime.BEFORE_ACTIVE)
                it.onActive()
                it.call(ManagerTime.ACTIVE)
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
        }
    }

    fun reload() {
        submit(async = true) {
            managers.forEach {
                try {
                    it.call(ManagerTime.BEFORE_RELOAD)
                    it.onReload()
                    it.call(ManagerTime.RELOAD)
                } catch (throwable: Throwable) {
                    throwable.printStackTrace()
                }
            }
        }
    }

    fun disable() {
        managers.forEach {
            try {
                it.call(ManagerTime.BEFORE_DISABLE)
                it.onDisable()
                it.call(ManagerTime.DISABLE)
            } catch (throwable: Throwable) {
                throwable.printStackTrace()

            }
        }
    }

}