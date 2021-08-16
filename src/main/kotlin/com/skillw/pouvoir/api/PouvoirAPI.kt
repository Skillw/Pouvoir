package com.skillw.pouvoir.api

import com.skillw.pouvoir.Pouvoir
import org.bukkit.entity.LivingEntity
import java.util.*

object PouvoirAPI {
    @JvmStatic
    fun replace(livingEntity: LivingEntity?, text: String): String {
        return Pouvoir.rpgPlaceHolderAPI.replace(livingEntity, text)
    }

    @JvmStatic
    fun replace(uuid: UUID, text: String): String {
        return Pouvoir.rpgPlaceHolderAPI.replace(uuid, text)
    }

    @JvmStatic
    fun analysis(text: String): String {
        return Pouvoir.functionManager.analysis(text).toString()
    }

    @JvmStatic
    fun analysis(entity: LivingEntity, text: String): String {
        return Pouvoir.functionManager.analysis(entity, text).toString()
    }

    @JvmStatic
    fun analysis(uuid: UUID, text: String): String {
        return Pouvoir.functionManager.analysis(uuid, text).toString()
    }
}