package com.skillw.pouvoir.util.nms

import com.skillw.pouvoir.util.BukkitAttribute
import org.bukkit.attribute.AttributeInstance
import org.bukkit.entity.LivingEntity
import taboolib.module.nms.nmsProxy

/**
 * @className NMS
 *
 * @author Glom
 * @date 2022/8/9 22:24 Copyright 2022 user. All rights reserved.
 */
abstract class NMS {

    abstract fun getAttribute(entity: LivingEntity, attribute: BukkitAttribute): AttributeInstance?

    companion object {
        
        val INSTANCE by lazy {
            nmsProxy<NMS>()
        }
    }

}