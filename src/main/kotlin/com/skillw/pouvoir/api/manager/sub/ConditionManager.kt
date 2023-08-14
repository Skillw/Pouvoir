package com.skillw.pouvoir.api.manager.sub

import com.skillw.pouvoir.api.feature.condition.BaseCondition
import com.skillw.pouvoir.api.feature.condition.ConditionData
import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.plugin.map.LowerKeyMap

/**
 * Condition manager
 *
 * @constructor Create empty Condition manager
 */
abstract class ConditionManager : LowerKeyMap<BaseCondition>(), Manager {

    /**
     * 匹配条件
     *
     * @param text String 字符串
     * @param slot String? 槽位
     * @return Collection<ConditionData> 条件数据
     */
    abstract fun matchConditions(text: String, slot: String?): Collection<ConditionData>

    /**
     * 匹配条件
     *
     * conditions例如:
     * ```
     *    conditions:
     *     - key: food
     *       value: 15
     *     - key: attribute
     *       name: 生命值
     *       value: 10
     *
     * ```
     *
     * @param conditions List<Map<String, Any>> 条件列表
     * @param slot String? 槽位
     * @return Collection<ConditionData> 条件数据
     */
    abstract fun matchConditions(conditions: List<Map<String, Any>>, slot: String?): Collection<ConditionData>
}
