package com.skillw.pouvoir.api.feature.handler

import com.skillw.pouvoir.Pouvoir.triggerHandlerManager
import com.skillw.pouvoir.api.feature.trigger.BaseTrigger
import com.skillw.pouvoir.api.plugin.map.component.Registrable

/**
 * @className BaseHandler
 *
 * 触发器处理器
 *
 * 负责处理与触发器相关的事务
 *
 * @author Glom
 * @date 2023/1/15 23:02 Copyright 2023 user. All rights reserved.
 */
abstract class BaseHandler<T : BaseTrigger>(
    override val key: String,
    vararg val triggers: String,
    val priority: Int = 3,
) :
    Registrable<String>, Comparable<BaseHandler<*>> {
    /**
     * 处理触发器
     *
     * @param trigger 触发器
     */
    abstract fun handle(trigger: T)

    /** 注册 */
    override fun register() {
        triggerHandlerManager.register(key, this)
    }

    /** 注销 */
    fun unregister() {
        triggerHandlerManager.unregister(key)
    }

    override fun compareTo(other: BaseHandler<*>): Int = if (this.priority == other.priority) 0
    else if (this.priority > other.priority) 1
    else -1

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BaseHandler<*>) return false
        if (key != other.key) return false

        return true
    }

    override fun hashCode(): Int {
        return key.hashCode()
    }

    override fun toString(): String {
        return "${javaClass.simpleName} { key = $key , priority = $priority }"
    }
}