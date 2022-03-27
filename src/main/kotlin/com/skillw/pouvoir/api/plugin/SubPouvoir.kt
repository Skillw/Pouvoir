package com.skillw.pouvoir.api.plugin

import com.skillw.pouvoir.api.able.Keyable
import com.skillw.pouvoir.api.manager.ManagerData
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.concurrent.ScheduledThreadPoolExecutor

interface SubPouvoir : Keyable<String> {
    var managerData: ManagerData
    val poolExecutor: ScheduledThreadPoolExecutor
    val plugin: JavaPlugin
    fun getConfigs(): MutableMap<String, com.skillw.pouvoir.util.Pair<File, YamlConfiguration>>

    fun load() {
        managerData.load()
    }

    fun enable() {
        managerData.enable()
    }

    fun active() {
        managerData.active()
    }

    fun disable() {
        managerData.disable()
    }

    override fun register() {
        TotalManager.register(this.plugin)
    }

    fun reload() {
        managerData.reload()
    }
}