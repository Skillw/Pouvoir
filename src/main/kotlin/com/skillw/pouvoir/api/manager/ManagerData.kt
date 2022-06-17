package com.skillw.pouvoir.api.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.able.Keyable
import com.skillw.pouvoir.api.manager.Manager.Companion.ACTIVE
import com.skillw.pouvoir.api.manager.Manager.Companion.BEFORE_ACTIVE
import com.skillw.pouvoir.api.manager.Manager.Companion.BEFORE_DISABLE
import com.skillw.pouvoir.api.manager.Manager.Companion.BEFORE_ENABLE
import com.skillw.pouvoir.api.manager.Manager.Companion.BEFORE_INIT
import com.skillw.pouvoir.api.manager.Manager.Companion.BEFORE_LOAD
import com.skillw.pouvoir.api.manager.Manager.Companion.BEFORE_RELOAD
import com.skillw.pouvoir.api.manager.Manager.Companion.DISABLE
import com.skillw.pouvoir.api.manager.Manager.Companion.ENABLE
import com.skillw.pouvoir.api.manager.Manager.Companion.INIT
import com.skillw.pouvoir.api.manager.Manager.Companion.LOAD
import com.skillw.pouvoir.api.manager.Manager.Companion.RELOAD
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
                run(it, BEFORE_INIT)
                it.onInit()
                run(it, INIT)
            } catch (throwable: Throwable) {
                throwable.printStackTrace()

            }
        }
        TotalManager.register(subPouvoir, this)
    }

    fun load() {
        Pouvoir.poolExecutor.execute {
            managers.forEach {
                try {
                    run(it, BEFORE_LOAD)
                    it.onLoad()
                    run(it, LOAD)
                } catch (throwable: Throwable) {
                    throwable.printStackTrace()

                }
            }
        }
    }

    fun enable() {
        Pouvoir.poolExecutor.execute {
            managers.forEach {
                try {
                    run(it, BEFORE_ENABLE)
                    it.onEnable()
                    run(it, ENABLE)
                } catch (throwable: Throwable) {
                    throwable.printStackTrace()

                }
            }
        }
    }

    fun active() {
        Pouvoir.poolExecutor.execute {
            managers.forEach {
                try {
                    run(it, BEFORE_ACTIVE)
                    it.onActive()
                    run(it, ACTIVE)
                } catch (throwable: Throwable) {
                    throwable.printStackTrace()

                }
            }
        }
    }

    fun reload() {
        Pouvoir.poolExecutor.execute {
            managers.forEach {
                try {
                    run(it, BEFORE_RELOAD)
                    it.onReload()
                    run(it, RELOAD)
                } catch (throwable: Throwable) {
                    throwable.printStackTrace()
                }
            }
        }
    }

    fun disable() {
        Pouvoir.poolExecutor.execute {
            managers.forEach {
                try {
                    run(it, BEFORE_DISABLE)
                    it.onDisable()
                    run(it, DISABLE)
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