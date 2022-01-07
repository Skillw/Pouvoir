package com.skillw.pouvoir.api.manager.sub

import com.skillw.pouvoir.api.attribute.BaseAttribute
import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.map.LowerKeyMap

abstract class BaseAttributeManager : LowerKeyMap<BaseAttribute>(), Manager {
    abstract val baseAttributes: MutableList<BaseAttribute>
    override val key = "BaseAttributeManager"
    override val priority: Int = 2

    override fun register(value: BaseAttribute) {
        map[value.key] = value
    }
}