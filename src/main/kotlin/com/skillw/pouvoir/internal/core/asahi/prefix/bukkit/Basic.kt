package com.skillw.pouvoir.internal.core.asahi.prefix.bukkit

import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.prefixParser
import com.skillw.asahi.api.quest
import com.skillw.asahi.api.quester
import com.skillw.pouvoir.internal.core.asahi.util.PlayerAsOp
import com.skillw.pouvoir.util.script.ColorUtil.decolored
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.function.adaptCommandSender
import taboolib.common.platform.function.console
import taboolib.module.chat.colored
import taboolib.module.chat.uncolored

/**
 * @className Basic
 *
 * @author Glom
 * @date 2023/1/23 17:54 Copyright 2023 user. All rights reserved.
 */

@AsahiPrefix(["command"])
private fun command() = prefixParser {
    val contentGetter = if (peek() == "[") questList() else questString()
    val whoGetter = if (expect("as")) {
        if (expect("op")) {
            quest<Player>().quester { PlayerAsOp(it) }
        } else questAny()
    } else quester { console() }
    result {
        val content = when (val origin = contentGetter.get()) {
            is List<*> -> origin.map { it.toString() }
            is String -> listOf(origin)
            else -> listOf(origin.toString())
        }
        val who = when (val origin = whoGetter.get()) {
            is ProxyCommandSender -> origin
            is CommandSender -> adaptCommandSender(origin)
            else -> console()
        }
        who.let {
            content.forEach { line ->
                it.performCommand(line.colored())
            }
        }
    }
}

@AsahiPrefix
private fun colored() = prefixParser {
    val message = quest<String>()
    result {
        message.get().colored()
    }
}

@AsahiPrefix
private fun decolored() = prefixParser {
    val message = quest<String>()
    result {
        message.get().decolored()
    }
}

@AsahiPrefix
private fun uncolor() = prefixParser {
    val message = quest<String>()
    result {
        message.get().uncolored()
    }
}