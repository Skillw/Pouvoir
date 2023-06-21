package com.skillw.pouvoir.api.plugin

import com.skillw.pouvoir.api.manager.ManagerData
import com.skillw.pouvoir.api.plugin.map.component.Registrable
import org.bukkit.plugin.java.JavaPlugin
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang

interface SubPouvoir : Registrable<String> {
    var managerData: ManagerData
    val plugin: JavaPlugin


    fun load() {
        managerData.load()
        console().sendLang("plugin-load", key)
    }

    fun enable() {
        managerData.enable()
        console().sendLang("plugin-enable", key)
    }

    fun active() {
        managerData.active()
    }

    fun disable() {
        managerData.disable()
        console().sendLang("plugin-disable", key)
    }

    override fun register() {
        TotalManager.register(this.managerData)
    }

    fun reload() {
        managerData.reload()
    }
}