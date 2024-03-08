package com.skillw.pouvoir.internal.core.asahi.prefix.bukkit

import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.prefixParser
import com.skillw.asahi.api.quest
import com.skillw.asahi.api.questType
import com.skillw.asahi.api.quester
import com.skillw.pouvoir.internal.core.asahi.util.delay
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.function.adaptCommandSender
import taboolib.common.util.unsafeLazy
import taboolib.common5.Coerce
import taboolib.library.xseries.XSound
import taboolib.module.chat.colored
import taboolib.platform.compat.VaultService
import java.util.concurrent.ConcurrentHashMap

/**
 * @className Player
 *
 * @author Glom
 * @date 2023/1/14 0:32 Copyright 2024 Glom.
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
private fun tell() = prefixParser<Unit> {
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
private fun actionbar() = prefixParser<Unit> {
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
private fun title() = prefixParser<Unit> {
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
private fun bossbar() = prefixParser<BossBar?> {
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


@AsahiPrefix(["permission", "perm"])
private fun permission() = prefixParser<Any?> {
    val playerGetter = if (expect("of")) quest<Player>() else quester { selector() }
    when (next()) {
        "add" -> {
            val permissions =
                if (peek() == "[")
                    questType<List<Any>>().quester { it.map { each -> each.toString() } }
                else questString().quester { listOf(it) }
            result {
                permissions.get().forEach {
                    VaultService.permission?.playerAdd(playerGetter.get(), it)
                }
            }
        }

        "remove" -> {
            val permissions =
                if (peek() == "[")
                    questType<List<Any>>().quester { it.map { each -> each.toString() } }
                else questString().quester { listOf(it) }
            result {
                permissions.get().forEach {
                    VaultService.permission?.playerRemove(playerGetter.get(), it)
                }
            }
        }

        "addGroup" -> {
            val groups =
                if (peek() == "[")
                    questType<List<Any>>().quester { it.map { each -> each.toString() } }
                else questString().quester { listOf(it) }
            result {
                groups.get().forEach {
                    VaultService.permission?.playerAddGroup(playerGetter.get(), it)
                }
            }
        }

        "removeGroup" -> {
            val groups =
                if (peek() == "[")
                    questType<List<Any>>().quester { it.map { each -> each.toString() } }
                else questString().quester { listOf(it) }
            result {
                groups.get().forEach {
                    VaultService.permission?.playerRemoveGroup(playerGetter.get(), it)
                }
            }
        }

        "inGroup" -> {
            val group = questString()
            result {
                VaultService.permission?.playerInGroup(playerGetter.get(), group.get())
            }
        }

        else -> {
            val permissions =
                if (peek() == "[")
                    questType<List<Any>>().quester { it.map { each -> each.toString() } }
                else questString().quester { listOf(it) }
            result {
                permissions.get().all {
                    VaultService.permission?.playerHas(playerGetter.get(), it) == true
                }
            }
        }
    }
}

private val economy by unsafeLazy {
    VaultService.economy!!
}

@AsahiPrefix(["money", "economy", "eco"])
private fun money() = prefixParser<Any> {
    val player = if (expect("of")) quest<Player>() else quester { selector() }
    when (val type = next()) {
        in arrayOf("add", "deposit") -> {
            val value = questDouble()
            result {
                economy.depositPlayer(player.get(), value.get())
            }
        }

        in arrayOf("take", "withdraw") -> {
            val value = questDouble()
            result {
                economy.withdrawPlayer(player.get(), value.get())
            }
        }

        "set" -> {
            val value = questDouble()
            result {
                player.get().let {
                    economy.withdrawPlayer(it, economy.getBalance(it))
                    economy.depositPlayer(it, value.get())
                }
            }
        }

        "has" -> {
            val value = questDouble()
            result {
                economy.has(player.get(), value.get())
            }
        }

        else -> {
            error("Unknown Economy Operation $type")
        }
    }
}