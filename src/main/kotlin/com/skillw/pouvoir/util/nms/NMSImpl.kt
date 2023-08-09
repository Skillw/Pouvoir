package com.skillw.pouvoir.util.nms

import com.skillw.pouvoir.util.attribute.BukkitAttribute
import net.minecraft.world.phys.AxisAlignedBB
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.attribute.AttributeInstance
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftEntity
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftLivingEntity
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.util.BoundingBox
import taboolib.library.reflex.Reflex.Companion.getProperty
import taboolib.module.nms.MinecraftVersion
import java.util.*
import java.util.function.Predicate


/**
 * @className NMS
 *
 * @author Glom
 * @date 2022/8/9 22:24 Copyright 2022 user. All rights reserved.
 */
class NMSImpl : NMS() {
    override fun getAttribute(entity: LivingEntity, attribute: BukkitAttribute): AttributeInstance? {
        return if (MinecraftVersion.major <= 4) {
            val craftAttributes = (entity as CraftLivingEntity).handle.craftAttributes
            val bukkitAtt = attribute.toBukkit() ?: return null
            craftAttributes.getAttribute(bukkitAtt)
        } else entity.getAttribute(attribute.toBukkit() ?: return null)
    }

    override fun getEntity(uuid: UUID): Entity? {
        Bukkit.getWorlds().forEach {
            it as CraftWorld
            val server = it.handle
            val lookup = server.getProperty<io.papermc.paper.chunk.system.entity.EntityLookup>("entityLookup")!!
            return lookup[uuid]?.bukkitEntity ?: return@forEach
        }
        return null
    }

    fun getEntities(world: World): Collection<Entity> {
        return world.entities
    }

    override fun getNearbyEntities(
        location: Location,
        x: Double,
        y: Double,
        z: Double,
        filter: Predicate<Entity>,
    ): Collection<Entity> {
        val boundingBox = BoundingBox.of(location, x, y, z)
        val bb = AxisAlignedBB(
            boundingBox.minX,
            boundingBox.minY,
            boundingBox.minZ,
            boundingBox.maxX,
            boundingBox.maxY,
            boundingBox.maxZ
        )
        val world = location.world as CraftWorld? ?: return emptyList()

        val entityList: List<net.minecraft.world.entity.Entity> = world.handle.a(null, bb) { true }
        val bukkitEntityList: MutableList<Entity> = ArrayList(entityList.size)
        val var6: Iterator<*> = entityList.iterator()
        while (true) {
            var bukkitEntity: CraftEntity
            do {
                if (!var6.hasNext()) {
                    return bukkitEntityList
                }
                val entity = var6.next() as net.minecraft.world.entity.Entity
                bukkitEntity = entity.bukkitEntity
            } while (!filter.test(bukkitEntity))
            bukkitEntityList.add(bukkitEntity)
        }
    }


}