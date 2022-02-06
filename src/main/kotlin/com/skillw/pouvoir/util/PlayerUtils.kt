package com.skillw.pouvoir.util

import com.skillw.pouvoir.Pouvoir
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import taboolib.common.reflect.Reflex.Companion.setProperty
import taboolib.module.nms.sendPacket
import taboolib.platform.BukkitAdapter


object PlayerUtils {

    @JvmStatic
    fun sendTitle(player: Player, title: String?, subtitle: String?, fadeIn: Int, stay: Int, fadeOut: Int) {
        val proxy = BukkitAdapter().adaptPlayer(player)
        proxy.sendTitle(title, subtitle, fadeIn, stay, fadeOut)
    }

    @JvmStatic
    fun sendTitle(player: Player, title: String?, fadeIn: Int, stay: Int, fadeOut: Int) {
        val proxy = BukkitAdapter().adaptPlayer(player)
        player.resetTitle()
        proxy.sendTitle(title, null, fadeIn, stay, fadeOut)
    }

    @JvmStatic
    fun sendTitle(player: Player, title: String?, subtitle: String?, stay: Int) {
        val proxy = BukkitAdapter().adaptPlayer(player)
        player.resetTitle()
        proxy.sendTitle(title, subtitle, 0, stay, 0)

    }

    @JvmStatic
    fun sendTitle(player: Player, title: String?, stay: Int) {
        val proxy = BukkitAdapter().adaptPlayer(player)
        player.resetTitle()
        proxy.sendTitle(title, null, 0, stay, 0)

    }

    @JvmStatic
    fun sendSubTitle(player: Player, subTitle: String?, stay: Int) {
        val proxy = BukkitAdapter().adaptPlayer(player)
        player.resetTitle()
        proxy.sendTitle(null, subTitle, 0, stay, 0)
    }

    @JvmStatic
    fun resetTitle(player: Player) {
        player.resetTitle()
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
    fun sendBossBar(player: Player, text: String, color: BarColor, style: BarStyle, progress: Double, ticks: Int) {
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
        object : BukkitRunnable() {
            override fun run() {
                bossBar.removePlayer(player)
                bossBar.removeAll()
            }
        }.runTaskLater(Pouvoir.plugin, ticks.toLong())
    }

    @JvmStatic
    fun sendBossBar(player: Player, text: String, ticks: Int) {
        sendBossBar(player, text, BarColor.PURPLE, BarStyle.SEGMENTED_10, 1.0, ticks)
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
        fields.forEach { (key, value) ->
            if (value != null) {
                any.setProperty(key, value)
            }
        }
        return any
    }
}