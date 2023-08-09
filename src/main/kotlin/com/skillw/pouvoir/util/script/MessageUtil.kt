package com.skillw.pouvoir.util.script

import com.skillw.pouvoir.util.isStaticClass
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import taboolib.library.reflex.Reflex.Companion.invokeMethod
import taboolib.module.chat.colored

/**
 * 给脚本用的
 *
 * ClassName : com.skillw.pouvoir.util Created by Glom_ on 2021-03-25
 * 20:25:13 Copyright 2021 user. All rights reserved.
 */
object MessageUtil {


    @JvmStatic
    fun message(sender: CommandSender, msg: String) {
        sender.sendMessage(msg.colored())
    }


    @JvmStatic
    fun warning(msg: Any?) {
        Bukkit.getLogger().warning(msg.toString().colored())
    }


    @JvmStatic
    fun info(msg: Any?) {
        Bukkit.getLogger().info(msg.toString().colored())
    }


    @JvmStatic
    fun debug(msg: Any?) {
        info("&9[&eDebug&9] &7${msg}")
    }


    @JvmStatic
    fun Any.information(key: String): String {
        return "&f$key  ${
            if (isStaticClass()) "&7= &6${
                this.invokeMethod<Class<*>>("getRepresentedClass")
                    ?.invokeMethod<String>("getName")
            }" else "&7: &6${javaClass.name}"
        }"
    }
}