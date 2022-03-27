package com.skillw.pouvoir.api.manager

import com.skillw.pouvoir.api.able.Keyable
import com.skillw.pouvoir.api.map.BaseMap
import com.skillw.pouvoir.api.map.KeyMap
import com.skillw.pouvoir.api.map.MultiExecMap
import com.skillw.pouvoir.api.plugin.SubPouvoir
import com.skillw.pouvoir.api.plugin.TotalManager
import com.skillw.pouvoir.internal.handle.PManagerHandle
import org.bukkit.plugin.java.JavaPlugin

class ManagerData(val subPouvoir: SubPouvoir) : KeyMap<String, Manager>(), Keyable<SubPouvoir> {
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
        for (manager in PManagerHandle.getPManagers(subPouvoir)) {
            manager.register(this)
        }
        val dataField = subPouvoir.javaClass.getField("managerData")
        dataField.set(subPouvoir, this)
    }

    override fun register() {
        managers.forEach {
            try {
                run(it, "BeforeInit")
                it.onInit()
                run(it, "Init")
            } catch (throwable: Throwable) {
                throwable.printStackTrace()

            }
        }
        TotalManager.register(subPouvoir, this)
    }

    fun load() {
        subPouvoir.poolExecutor.execute {
            managers.forEach {
                try {
                    run(it, "BeforeLoad")
                    it.onLoad()
                    run(it, "Load")
                } catch (throwable: Throwable) {
                    throwable.printStackTrace()

                }
            }
        }
    }

    fun enable() {
        subPouvoir.poolExecutor.execute {
            managers.forEach {
                try {
                    run(it, "BeforeEnable")
                    it.onEnable()
                    run(it, "Enable")
                } catch (throwable: Throwable) {
                    throwable.printStackTrace()

                }
            }
        }
    }

    fun active() {
        subPouvoir.poolExecutor.execute {
            managers.forEach {
                try {
                    run(it, "BeforeActive")
                    it.onActive()
                    run(it, "Active")
                } catch (throwable: Throwable) {
                    throwable.printStackTrace()

                }
            }
        }
    }

    fun reload() {
        subPouvoir.poolExecutor.execute {
            managers.forEach {
                try {
                    run(it, "BeforeReload")
                    it.onReload()
                    run(it, "Reload")
                } catch (throwable: Throwable) {
                    throwable.printStackTrace()
                }
            }
        }
    }

    fun disable() {
        subPouvoir.poolExecutor.execute {
            managers.forEach {
                try {
                    run(it, "BeforeDisable")
                    it.onDisable()
                    run(it, "Disable")
                } catch (throwable: Throwable) {
                    throwable.printStackTrace()

                }
            }
        }
    }

    internal fun run(manager: Manager, thing: String) {
        if (!exec.containsKey(manager)) return
        exec[manager]!!.run(thing)
    }
}