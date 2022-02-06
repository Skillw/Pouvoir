package com.skillw.pouvoir.util

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import taboolib.module.chat.colored

/**
 * ClassName : com.skillw.classsystem.util.MessageUtils
 * Created by Glom_ on 2021-03-25 20:25:13
 * Copyright  2021 user. All rights reserved.
 */
object MessageUtils {
    @JvmStatic
    fun message(sender: CommandSender, msg: String) {
        sender.sendMessage(msg.colored())
    }

    @JvmStatic
    fun wrong(msg: String) {
        Bukkit.getLogger().warning(msg.colored())
    }

    @JvmStatic
    fun info(msg: String) {
        Bukkit.getLogger().info(msg.colored())
    }

    @JvmStatic
    fun debug(msg: String) {
        info("§9[§eDebug§9] §e${msg.colored()}")
    }
}