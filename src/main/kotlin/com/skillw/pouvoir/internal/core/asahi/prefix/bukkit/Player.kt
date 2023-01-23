package com.skillw.pouvoir.internal.core.asahi.prefix.bukkit

import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.prefixParser
import com.skillw.asahi.api.quest
import com.skillw.asahi.api.quester
import com.skillw.pouvoir.internal.core.asahi.util.delay
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.command.CommandSender
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.function.adaptCommandSender
import taboolib.common5.Coerce
import taboolib.library.xseries.XSound
import taboolib.module.chat.colored
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * @className Player
 *
 * @author Glom
 * @date 2023/1/14 0:32 Copyright 2023 user. All rights reserved.
 */

@AsahiPrefix
private fun sound() = prefixParser<Sound> {
    val sound = quest<String>()
    result {
        val id = sound.get()
        XSound.matchXSound(id)
            .run { if (this.isPresent) get().parseSound() else Coerce.toEnum(id, Sound::class.java) }
            ?: error("Sound not found: $id")
    }
}

@AsahiPrefix(["tell", "send", "message"])
private fun tell() = prefixParser {
    val targets = if (expect("all")) quester { Bukkit.getOnlinePlayers() } else questAny()
    val contentGetter = if (peek() == "[") questList() else questString()
    result {
        val content = when (val origin = contentGetter.get()) {
            is Collection<*> -> origin.map { it.toString() }
            is String -> listOf(origin)
            else -> listOf(origin.toString())
        }
        val senders = when (val origin = targets.get()) {
            is Collection<*> -> origin.mapNotNull { it?.let { it1 -> adaptCommandSender(it1) } }
            is CommandSender -> listOf(adaptCommandSender(origin))
            is ProxyCommandSender -> listOf(origin)
            else -> emptyList()
        }
        senders.forEach {
            content.forEach { line ->
                it.sendMessage(line.colored())
            }
        }
    }
}

@AsahiPrefix(["actionbar"])
private fun actionbar() = prefixParser {
    val targets = if (expect("all")) quester { Bukkit.getOnlinePlayers() } else questAny()
    val period = if (expect("every")) questTick() else quester { 20L }
    val contentGetter = if (peek() == "[") questList() else questString()
    result {
        val content = when (val origin = contentGetter.get()) {
            is List<*> -> origin.map { it.toString() }
            is String -> listOf(origin)
            else -> listOf(origin.toString())
        }
        val senders = when (val origin = targets.get()) {
            is Collection<*> -> origin.mapNotNull { it?.let { it1 -> adaptCommandSender(it1) } }
            is CommandSender -> listOf(adaptCommandSender(origin))
            is ProxyCommandSender -> listOf(origin)
            else -> emptyList()
        }
        senders.forEach {
            if (it !is ProxyPlayer) return@forEach
            content.forEachIndexed { index, line ->
                it.sendActionBar(line.colored())
                if (index != content.lastIndex)
                    delay(period.get())
            }
        }
    }
}

@AsahiPrefix(["title"])
private fun title() = prefixParser {
    val targets = if (expect("all")) quester { Bukkit.getOnlinePlayers() } else questAny()
    //主标题
    val title = quest<String?>()
    //副标题
    val subTitle = quest<String?>()
    //淡入时间
    var fadeIn = quester { 0 }
    //停留时间
    var stay = quester { 20 }
    //淡出时间
    var fadeOut = quester { 0 }
    if (expect("with")) {
        fadeIn = questTick().quester { it.toInt() }
        stay = questTick().quester { it.toInt() }
        fadeOut = questTick().quester { it.toInt() }
    }
    result {
        val senders = when (val origin = targets.get()) {
            is Collection<*> -> origin.mapNotNull { it?.let { it1 -> adaptCommandSender(it1) } }
            is CommandSender -> listOf(adaptCommandSender(origin))
            is ProxyCommandSender -> listOf(origin)
            else -> emptyList()
        }
        senders.forEach {
            if (it !is ProxyPlayer) return@forEach
            it.sendTitle(
                title.get(),
                subTitle.get(),
                fadeIn.get(),
                stay.get(),
                fadeOut.get()
            )
        }
    }
}

private val bossBars = ConcurrentHashMap<String, BossBar>()

@AsahiPrefix(["bossbar"])
private fun bossbar() = prefixParser {
    if (expect("release")) {
        val barKeyGetter = questString()
        return@prefixParser result {
            val key = barKeyGetter.get()
            bossBars[key]?.removeAll()
            bossBars.remove(key)
        }
    }
    val bar = questString().quester {
        bossBars.computeIfAbsent(it) {
            Bukkit.createBossBar(
                "",
                BarColor.BLUE,
                BarStyle.SEGMENTED_10
            )
        }
    }
    result {
        bar.get()
    }
}