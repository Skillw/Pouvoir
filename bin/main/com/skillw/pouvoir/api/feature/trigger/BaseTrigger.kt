package com.skillw.pouvoir.api.feature.trigger

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.plugin.map.component.Keyable

/**
 * @className BaseTrigger
 *
 * 基础触发器
 *
 * 如果想开发自定义触发器，你需要:
 * - 继承一个TriggerController 并@AutoRegister
 * - 继承BaseTrigger，声明一个非抽象子类，用于实例化触发器
 * - 调用TriggerManager相关API，完成 触发->处理 流程
 *
 * @author Glom
 * @date 2023/1/6 22:59 Copyright 2024 Glom.
 */
abstract class BaseTrigger(override val key: String) : Keyable<String> {
    /** 触发 */
    fun call() {
        Pouvoir.triggerManager.call(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BaseTrigger) return false

        if (key != other.key) return false

        return true
    }

    override fun hashCode(): Int {
        return key.hashCode()
    }

    override fun toString(): String {
        return "Trigger{ $key }"
    }

}