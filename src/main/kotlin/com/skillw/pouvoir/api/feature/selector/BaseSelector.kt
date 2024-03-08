package com.skillw.pouvoir.api.feature.selector

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.plugin.map.DataMap
import com.skillw.pouvoir.api.plugin.map.component.Registrable

/**
 * @className BaseSelector
 *
 * 目标选择器，具有以下功能:
 * - 选择目标
 * - 筛选目标
 * - 从可变数组装填参数
 *
 * @author Glom
 * @date 2023/1/6 23:02 Copyright 2024 Glom.
 */
abstract class BaseSelector(override val key: String) : Registrable<String> {
    /**
     * 目标选择器上下文
     *
     * @param consumer 处理
     * @constructor
     * @property result 结果
     */
    class SelectorContext(
        val result: SelectResult = SelectResult(),
        consumer: (DataMap) -> Unit = {},
    ) : DataMap() {
        init {
            consumer(this)
        }
    }

    /**
     * 目标选择器 - 目标选择
     *
     * @param caster 释放者
     */
    protected open fun SelectorContext.select(caster: Target) {}

    /**
     * 目标选择器 - 目标筛选
     *
     * @param caster 释放者
     */
    protected open fun SelectorContext.filter(caster: Target) {}

    /**
     * 目标选择器 - 目标反筛选
     *
     * @param caster 释放者
     */
    protected open fun SelectorContext.except(caster: Target) {}

    /**
     * 从可变数组装填参数
     *
     * @param dataMap 参数
     * @param args 可变数组
     */
    protected open fun addParameter(dataMap: DataMap, vararg args: Any?) {}

    /**
     * 目标选择
     *
     * @param result 结果
     * @param caster 释放者
     * @param args 参数
     * @receiver
     */
    fun select(result: SelectResult, caster: Target, vararg args: Any?) {
        SelectorContext(result) {
            addParameter(it, *args)
        }.apply {
            select(caster)
        }
    }

    /**
     * 目标筛选
     *
     * @param result 结果
     * @param caster 释放者
     * @param args 参数
     */
    fun filter(result: SelectResult, caster: Target, vararg args: Any?) {
        SelectorContext(result) {
            addParameter(it, *args)
        }.apply {
            filter(caster)
        }
    }

    /**
     * 目标反筛选
     *
     * @param result 结果
     * @param caster 释放者
     * @param args 参数
     */
    fun except(result: SelectResult, caster: Target, vararg args: Any?) {
        SelectorContext(result) {
            addParameter(it, *args)
        }.apply {
            except(caster)
        }
    }

    /**
     * 目标选择
     *
     * @param result 结果
     * @param caster 释放者
     * @param map 参数
     * @receiver
     */
    fun select(result: SelectResult, caster: Target, map: Map<String, Any>) {
        SelectorContext(result) {
            it.putAll(map)
        }.apply {
            select(caster)
        }
    }

    /**
     * 目标筛选
     *
     * @param result 结果
     * @param caster 释放者
     * @param map 参数
     */
    fun filter(result: SelectResult, caster: Target, map: Map<String, Any>) {
        SelectorContext(result) {
            it.putAll(map)
        }.apply {
            filter(caster)
        }
    }

    /**
     * 目标反筛选
     *
     * @param result 结果
     * @param caster 释放者
     * @param map 参数
     */
    fun except(result: SelectResult, caster: Target, map: Map<String, Any>) {
        SelectorContext(result) {
            it.putAll(map)
        }.apply {
            except(caster)
        }
    }

    override fun register() {
        Pouvoir.selectorManager.register(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BaseSelector) return false

        if (key != other.key) return false

        return true
    }

    override fun hashCode(): Int {
        return key.hashCode()
    }

    override fun toString(): String {
        return "Selector{ $key }"
    }

}