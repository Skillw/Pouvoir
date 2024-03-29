package com.skillw.pouvoir.util.attribute

import com.skillw.pouvoir.util.nms.NMS
import org.bukkit.attribute.AttributeInstance
import org.bukkit.entity.LivingEntity


fun LivingEntity.getAttribute(attribute: BukkitAttribute): AttributeInstance? {
    return NMS.INSTANCE.getAttribute(this, attribute)
}


fun AttributeInstance.clear() {
    for (modifier in this.modifiers) {
        this.removeModifier(modifier)
    }
}
