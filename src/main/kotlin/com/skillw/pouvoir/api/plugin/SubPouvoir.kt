package com.skillw.pouvoir.api.plugin

import com.skillw.pouvoir.api.able.Registrable
import com.skillw.pouvoir.api.manager.ManagerData
import org.bukkit.plugin.java.JavaPlugin
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang

interface SubPouvoir : Registrable<String> {
    var managerData: ManagerData
    val plugin: JavaPlugin


    fun load() {
        console().sendLang("plugin-load", key)
        managerData.load()
    }

    fun enable() {
        console().sendLang("plugin-enable", key)
        managerData.enable()
    }

    fun active() {
        managerData.active()
    }

    fun disable() {
        console().sendLang("plugin-disable", key)
        managerData.disable()
    }

    override fun register() {
        TotalManager.register(this.managerData)
    }

    fun reload() {
        managerData.reload()
    }
}