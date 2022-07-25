package com.skillw.pouvoir.util

import com.skillw.pouvoir.api.annotation.ScriptTopLevel
import org.bukkit.command.CommandSender
import taboolib.module.chat.colored

/**
 *
 * For script coders
 *
 * ClassName : com.skillw.classsystem.util.MessageUtils
 * Created by Glom_ on 2021-03-25 20:25:13
 * Copyright  2021 user. All rights reserved.
 */
object MessageUtils {
    @JvmStatic
    @ScriptTopLevel
    fun message(sender: CommandSender, msg: String) {
        sender.sendMessage(msg.colored())
    }

    @JvmStatic
    @ScriptTopLevel
    fun warning(msg: Any?) {
        taboolib.common.platform.function.warning(msg.toString().colored())
    }

    @JvmStatic
    @ScriptTopLevel
    fun info(msg: Any?) {
        taboolib.common.platform.function.info(msg.toString().colored())
    }

    @JvmStatic
    @ScriptTopLevel
    fun debug(msg: Any?) {
        info("&9[&eDebug&9] &e${msg}")
    }
}