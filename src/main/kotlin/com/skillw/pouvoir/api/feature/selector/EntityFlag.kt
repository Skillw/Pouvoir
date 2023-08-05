package com.skillw.pouvoir.api.feature.selector

import com.skillw.pouvoir.api.plugin.map.BaseMap
import org.bukkit.entity.Entity
import taboolib.common.platform.function.submit
import taboolib.platform.util.removeMeta
import taboolib.platform.util.setMeta

/**
 * Entity flag
 *
 * 实体标记器，负责标记实体
 *
 * @constructor Create empty Entity flag
 */
object EntityFlag {
    private val keyToEntity = BaseMap<String, LinkedHashSet<Entity>>()

    fun addFlag(entity: Entity, key: String, durationTick: Long = -1) {
        entity.setMeta(key, "POU_FLAG")
        keyToEntity.computeIfAbsent(key) { LinkedHashSet() }.add(entity)
        if (durationTick > 0)
            submit(delay = durationTick) {
                entity.removeFlag(key)
            }
    }

    fun getEntities(flag: String): Set<Entity> {
        return LinkedHashSet<Entity>().apply {
            keyToEntity[flag]?.let { flagEntities ->
                addAll(flagEntities.filter { !it.isDead && it.isValid })
                flagEntities.filter { it !in this }.forEach(flagEntities::remove)
            }
        }
    }

    fun removeFlag(entity: Entity, key: String) {
        entity.removeMeta(key)
        keyToEntity[key]?.remove(entity)
    }
}

