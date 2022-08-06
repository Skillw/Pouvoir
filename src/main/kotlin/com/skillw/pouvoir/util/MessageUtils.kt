package com.skillw.pouvoir.util

import com.skillw.pouvoir.api.annotation.ScriptTopLevel
import com.skillw.pouvoir.util.ClassUtils.isStaticClass
import taboolib.library.reflex.Reflex.Companion.invokeMethod
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import taboolib.module.chat.colored

/**
 * For script coders
 *
 * ClassName : com.skillw.classsystem.util.MessageUtils Created by Glom_ on
 * 2021-03-25 20:25:13 Copyright 2021 user. All rights reserved.
 */
object MessageUtils {
    @JvmStatic
    fun message(sender: CommandSender, msg: String) {
        sender.sendMessage(msg.colored())
    }

    @JvmStatic
    @ScriptTopLevel
    fun warning(msg: Any?) {
        Bukkit.getLogger().warning(msg.toString().colored())
    }

    @JvmStatic
    @ScriptTopLevel
    fun info(msg: Any?) {
        Bukkit.getLogger().info(msg.toString().colored())
    }

    @JvmStatic
    @ScriptTopLevel
    fun debug(msg: Any?) {
        info("&9[&eDebug&9] &e${msg}")
    }

    @JvmStatic
    internal fun Any.information(key: String): String {
        return "&f$key  ${
            if (isStaticClass()) "&7= &6${
                this.invokeMethod<Class<*>>("getRepresentedClass")
                    ?.invokeMethod<String>("getName")
            }" else "&7: &6${javaClass.name}"
        }"
    }
}