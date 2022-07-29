package com.skillw.pouvoir.api.manager

import com.skillw.pouvoir.api.able.Registrable
import com.skillw.pouvoir.api.event.ManagerTime
import com.skillw.pouvoir.api.event.PouManagerEvent
import com.skillw.pouvoir.api.map.BaseMap
import com.skillw.pouvoir.api.map.KeyMap
import com.skillw.pouvoir.api.map.MultiExecMap
import com.skillw.pouvoir.api.plugin.SubPouvoir
import com.skillw.pouvoir.api.plugin.TotalManager
import com.skillw.pouvoir.internal.plugin.PouManagerUtils.getPouManagers
import org.bukkit.plugin.java.JavaPlugin
import taboolib.common.platform.function.submit

class ManagerData(val subPouvoir: SubPouvoir) : KeyMap<String, Manager>(), Registrable<SubPouvoir> {
    val exec = BaseMap<Manager, MultiExecMap>()
    private val managers = ArrayList<Manager>()
    val plugin: JavaPlugin = subPouvoir.plugin
    override val key: SubPouvoir = subPouvoir

    override fun register(key: String, value: Manager) {
        super.register(key, value)
        managers.add(value)
        managers.sort()
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
        submit(async = true) {
            managers.forEach {
                try {
                    call(it, ManagerTime.BEFORE_LOAD)
                    it.onLoad()
                    call(it, ManagerTime.LOAD)
                } catch (throwable: Throwable) {
                    throwable.printStackTrace()

                }
            }
        }
    }

    fun enable() {
        submit(async = true) {
            managers.forEach {
                try {
                    call(it, ManagerTime.BEFORE_ENABLE)
                    it.onEnable()
                    call(it, ManagerTime.ENABLE)
                } catch (throwable: Throwable) {
                    throwable.printStackTrace()

                }
            }
        }
    }

    fun active() {
        submit(async = true) {
            managers.forEach {
                try {
                    call(it, ManagerTime.BEFORE_ACTIVE)
                    it.onActive()
                    call(it, ManagerTime.ACTIVE)
                } catch (throwable: Throwable) {
                    throwable.printStackTrace()

                }
            }
        }
    }

    fun reload() {
        submit(async = true) {
            managers.forEach {
                try {
                    call(it, ManagerTime.BEFORE_RELOAD)
                    it.onReload()
                    call(it, ManagerTime.RELOAD)
                } catch (throwable: Throwable) {
                    throwable.printStackTrace()
                }
            }
        }
    }

    fun disable() {
        submit(async = true) {
            managers.forEach {
                try {
                    call(it, ManagerTime.BEFORE_DISABLE)
                    it.onDisable()
                    call(it, ManagerTime.DISABLE)
                } catch (throwable: Throwable) {
                    throwable.printStackTrace()

                }
            }
        }
    }

    internal fun call(manager: Manager, time: ManagerTime) {
        PouManagerEvent(manager, time).call()
    }
}