package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.feature.condition.ConditionData
import com.skillw.pouvoir.api.manager.sub.ConditionManager
import com.skillw.pouvoir.util.clone


object ConditionManagerImpl : ConditionManager() {
    override val key = "ConditionManager"
    override val priority: Int = 7
    override val subPouvoir = Pouvoir

    override fun onEnable() {
        onReload()
    }

    override fun onReload() {
        this.entries.filter { it.value.release }.forEach { this.remove(it.key) }
    }

    override fun matchConditions(text: String, slot: String?): Collection<ConditionData> =
        ArrayList<ConditionData>().apply {
            values.forEach { condition ->
                condition.parameters(text)?.let {
                    this += ConditionData(condition).push(HashMap(it).apply { put("slot", slot) })
                }
            }
        }

    override fun matchConditions(conditions: List<Map<String, Any>>, slot: String?): Collection<ConditionData> =
        ArrayList<ConditionData>().apply {
            conditions.forEach { map ->
                val key = map["key"].toString()
                val condition = get(key) ?: return@forEach
                val args = map.clone() as MutableMap<String, Any>
                slot?.let { args["slot"] = it }
                this += ConditionData(condition).push(args)
            }
        }


}
