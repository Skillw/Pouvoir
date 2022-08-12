package com.skillw.pouvoir.util

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import taboolib.library.reflex.Reflex.Companion.setProperty
import taboolib.module.nms.sendPacket
import taboolib.platform.BukkitAdapter

// For script coders
object PlayerUtils {

    @JvmStatic
    fun sendTitle(
        player: Player,
        title: String?,
        subtitle: String?,
        fadeIn: Int = 0,
        stay: Int = 20,
        fadeOut: Int = 0,
    ) {
        val proxy = BukkitAdapter().adaptPlayer(player)
        player.resetTitle()
        proxy.sendTitle(title, subtitle, fadeIn, stay, fadeOut)
    }

    @JvmStatic
    fun sendActionBar(player: Player, text: String) {
        val proxy = BukkitAdapter().adaptPlayer(player)
        proxy.sendActionBar(text)
    }

    @JvmStatic
    fun sendActionBar(player: Player, text: String, stay: Long, javaPlugin: JavaPlugin = BukkitAdapter().plugin) {
        val proxy = BukkitAdapter().adaptPlayer(player)
        proxy.sendActionBar(text)
        Bukkit.getScheduler().runTaskLater(javaPlugin, Runnable { proxy.sendActionBar("") }, stay)
    }

    @JvmStatic
    fun resetActionBar(player: Player) {
        val proxy = BukkitAdapter().adaptPlayer(player)
        proxy.sendActionBar("")
    }

    @JvmStatic
    fun sendBossBar(player: Player, text: String, color: BarColor, style: BarStyle, progress: Double): BossBar {
        var progress1 = progress
        if (progress1 < 0) {
            progress1 = 0.0
        }
        if (progress1 > 1) {
            progress1 = 1.0
        }
        val bossBar = Bukkit.createBossBar(text, color, style)
        bossBar.progress = progress1
        bossBar.addPlayer(player)
        return bossBar
    }

    @JvmStatic
    fun setCooldown(player: Player, material: Material?, cooldown: Int) {
        player.setCooldown(material!!, cooldown)
    }

    @JvmStatic
    fun getCooldown(player: Player, material: Material?) {
        player.getCooldown(material!!)
    }

    @JvmStatic
    fun hasCooldown(player: Player, material: Material?): Boolean {
        return player.hasCooldown(material!!)
    }

    @JvmStatic
    fun removeCooldown(player: Player, material: Material?) {
        player.setCooldown(material!!, 0)
    }

    @JvmStatic
    fun Player.sendPacketWithFields(packet: Any, vararg fields: Pair<String, Any?>) {
        this.sendPacket(setFields(packet, *fields))
    }

    private fun setFields(any: Any, vararg fields: Pair<String, Any?>): Any {
        fields.forEach { pair ->
            val key = pair.key
            val value = pair.value
            value?.also {
                any.setProperty(key, value)
            }
        }
        return any
    }
}