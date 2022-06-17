package com.skillw.pouvoir.api.manager.sub

import com.skillw.pouvoir.api.manager.Manager
import org.bukkit.entity.LivingEntity
import java.util.*

/**
 * ClassName : com.skillw.pouvoir.api.PouPlaceHolderAPI
 * Created by Glom_ on 2021-03-28 15:41:27
 * Copyright  2021 user. All rights reserved.
 */
interface PouPlaceHolderAPI : Manager {
    fun replace(livingEntity: LivingEntity?, text: String): String
    fun replace(uuid: UUID, text: String): String
}