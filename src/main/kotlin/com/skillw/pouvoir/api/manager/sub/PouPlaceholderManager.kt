package com.skillw.pouvoir.api.manager.sub

import com.skillw.pouvoir.api.feature.placeholder.PouPlaceHolder
import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.plugin.map.KeyMap
import org.bukkit.entity.LivingEntity
import java.util.*

/**
 * PouPlaceholderManager
 *
 * 占位符管理器
 *
 * 主要负责:
 * - 维护占位符
 * - 进行文本中占位符的替换
 *
 * @constructor Create empty Pou placeholder manager
 */
abstract class PouPlaceholderManager : KeyMap<String, PouPlaceHolder>(), Manager {
    /**
     * 替换占位符
     *
     * @param entity 实体
     * @param text 待处理的文本
     * @return 处理后的文本
     */
    fun replace(entity: LivingEntity?, text: String): String {
        return replace(entity, text, true)
    }

    /**
     * 替换占位符
     *
     * @param uuid 实体的UUID
     * @param text 待处理的文本
     * @return 处理后的文本
     */
    abstract fun replace(uuid: UUID, text: String): String

    /**
     * 替换占位符
     *
     * @param entity
     * @param text
     * @param analysis
     * @return
     */
    abstract fun replace(entity: LivingEntity?, text: String, analysis: Boolean): String
}