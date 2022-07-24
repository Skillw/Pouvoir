package com.skillw.pouvoir.api.plugin

import com.skillw.pouvoir.api.able.Registrable
import com.skillw.pouvoir.api.manager.ManagerData
import com.skillw.pouvoir.util.MessageUtils
import org.bukkit.plugin.java.JavaPlugin

interface SubPouvoir : Registrable<String> {
    var managerData: ManagerData
    val plugin: JavaPlugin


    fun load() {
        MessageUtils.info("&d[&9$key&d] &a$key is loaded...")
        managerData.load()
    }

    fun enable() {
        MessageUtils.info("&d[&9$key&d] &e$key is enable...")
        managerData.enable()
    }

    fun active() {
        managerData.active()
    }

    fun disable() {
        MessageUtils.info("&d[&9$key&d] &c$key is disable...")
        managerData.disable()
    }

    override fun register() {
        TotalManager.register(this.managerData)
    }

    fun reload() {
        managerData.reload()
    }
}