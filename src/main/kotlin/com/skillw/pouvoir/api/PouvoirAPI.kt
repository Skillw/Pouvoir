package com.skillw.pouvoir.api

import com.skillw.pouvoir.Pouvoir
import org.bukkit.entity.LivingEntity

@Suppress("UNCHECKED_CAST")
object PouvoirAPI {

    /**
     * 解析占位符(PouPAPI & PAPI)
     *
     * @param entity LivingEntity 实体
     * @param analysis Boolean 是否解析Asahi
     * @return String 解析后的文本
     * @receiver String 待解析的文本
     */
    @JvmStatic
    fun String.placeholder(entity: LivingEntity, analysis: Boolean = true): String {
        return Pouvoir.placeholderManager.replace(entity, this, analysis)
    }

}