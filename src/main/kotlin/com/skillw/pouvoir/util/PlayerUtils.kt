package com.skillw.pouvoir.util

import com.skillw.pouvoir.Pouvoir
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
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
    fun getEntityRayHit(
        livingEntity: LivingEntity, range: Double
    ): LivingEntity? {
        fun legacy(): LivingEntity? {
            fun rotateXz(origin: Vector, direction: Vector): Vector {
                val x = origin.x * direction.x - origin.z * direction.z
                val y = origin.y
                val z = origin.x * direction.z + origin.z * direction.x
                return Vector(x, y, z)
            }

            fun isInLoc(loc1: Location, loc2: Location): Boolean {
                var x1 = loc1.blockX - loc2.blockX
                var z1 = loc1.blockZ - loc2.blockZ
                var y1 = loc1.blockY - loc2.blockY
                if (y1 < 0) {
                    y1 = -y1
                }
                if (x1 < 0) {
                    x1 = -x1
                }
                if (z1 < 0) {
                    z1 = -z1
                }
                return x1 <= 0.5 && z1 <= 0.5 && y1 <= 1.5
            }

            val entities = livingEntity.getNearbyEntities(range, range, range)
            val livingEntities = ArrayList<LivingEntity>()
            for (ent in entities) {
                if (ent is LivingEntity) {
                    livingEntities.add(ent)
                }
            }
            val loc = livingEntity.eyeLocation
            val v = Vector(2.0, (0.0f - loc.pitch) * 0.03, 0.0)
            val a = livingEntity.location.direction
            val vc = rotateXz(v, a)
            var i = 1
            while (i < range) {
                for (lic in livingEntities) {
                    if (isInLoc(loc, lic.location)) {
                        return if (lic !is ArmorStand) lic else null
                    }
                }
                loc.add(vc)
                i++
            }
            return null
        }

        fun new(): LivingEntity? {
            val world = livingEntity.world
            val loc = livingEntity.eyeLocation.clone()
            val direction = loc.direction.clone()
            loc.add(direction)
            val rayTraceEntities = world.rayTraceEntities(loc, loc.direction, range)
            if (rayTraceEntities != null) {
                val hitEntity = rayTraceEntities.hitEntity
                return if (EntityUtils.isLiving(hitEntity)) hitEntity as LivingEntity else null
            }
            return null
        }
        return if (Pouvoir.isLegacy) legacy() else new()
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
}