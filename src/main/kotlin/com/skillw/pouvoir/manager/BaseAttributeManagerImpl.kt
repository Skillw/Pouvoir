package com.skillw.pouvoir.manager

import com.skillw.pouvoir.api.attribute.BaseAttribute
import com.skillw.pouvoir.api.event.AttributeRegisterEvent
import com.skillw.pouvoir.api.event.Time
import com.skillw.pouvoir.api.manager.sub.BaseAttributeManager
import java.util.*

object BaseAttributeManagerImpl : BaseAttributeManager() {
    override val baseAttributes: MutableList<BaseAttribute> by lazy {
        Collections.synchronizedList(ArrayList())
    }

    override fun register(key: String, value: BaseAttribute) {
        val before = AttributeRegisterEvent(Time.BEFORE, value)
        before.call()
        if (before.isCancelled) return
        baseAttributes.add(value)
        baseAttributes.sort()
        put(key, value)
        val after = AttributeRegisterEvent(Time.AFTER, value)
        after.call()
    }
}