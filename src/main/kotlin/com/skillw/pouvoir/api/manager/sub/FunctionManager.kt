package com.skillw.pouvoir.api.manager.sub

import com.skillw.pouvoir.api.function.Function
import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.map.KeyMap
import org.bukkit.entity.LivingEntity
import java.util.*

abstract class FunctionManager : KeyMap<String, Function>(), Manager {
    override val priority = 1
    abstract fun analysis(text: String): String?
    abstract fun analysis(entity: LivingEntity, text: String): String?
    abstract fun analysis(uuid: UUID, text: String): String?
}