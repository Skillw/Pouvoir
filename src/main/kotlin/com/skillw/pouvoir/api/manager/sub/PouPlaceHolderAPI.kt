package com.skillw.pouvoir.api.manager.sub

import com.skillw.pouvoir.api.manager.Manager
import org.bukkit.entity.LivingEntity
import java.util.*

/**
 * ClassName : com.skillw.pouvoir.api.PouPlaceHolderAPI Created by Glom_ on
 * 2021-03-28 15:41:27 Copyright 2021 user. All rights reserved.
 */
interface PouPlaceHolderAPI : Manager {
    /**
     * Replace
     *
     * @param entity 实体
     * @param text 待处理的文本
     * @return 处理后的文本
     */
    fun replace(entity: LivingEntity?, text: String): String

    /**
     * Replace
     *
     * @param uuid 实体的UUID
     * @param text 待处理的文本
     * @return 处理后的文本
     */
    fun replace(uuid: UUID, text: String): String

}