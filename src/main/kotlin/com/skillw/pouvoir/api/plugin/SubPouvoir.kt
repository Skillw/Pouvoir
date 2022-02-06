package com.skillw.pouvoir.api.plugin

import com.skillw.pouvoir.api.able.Keyable
import com.skillw.pouvoir.api.able.Pluginable
import com.skillw.pouvoir.api.manager.ManagerData
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.util.concurrent.ScheduledExecutorService

interface SubPouvoir : Pluginable, Keyable<String> {
    var managerData: ManagerData
    val poolExecutor: ScheduledExecutorService

    fun getConfigs(): MutableMap<String, Pair<File, YamlConfiguration>>

    override fun register() {
        TotalManager.register(this.plugin)
    }

    fun reloadAll() {
        TotalManager.reload(this)
    }
}