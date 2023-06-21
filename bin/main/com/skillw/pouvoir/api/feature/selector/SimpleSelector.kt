package com.skillw.pouvoir.api.feature.selector

/**
 * @className SimpleSelector
 *
 * 简易选取器 - 用于快速开发选择器
 *
 * 你只需要覆写 SelectorContext.getTargets(返回一个列表) 与 addParameters 即可实现以下功能:
 * - 目标选择 -> 添加所有列表中的目标
 * - 目标筛选 -> 删除不在列表中的目标
 * - 目标反筛选 -> 删除在列表中的目标
 *
 * @author Glom
 * @date 2023/1/6 23:02 Copyright 2023 user. All rights reserved.
 */
abstract class SimpleSelector(key: String) : BaseSelector(key) {
    /**
     * 获取目标
     *
     * 你只需要覆写 此方法 与 addParameters 即可实现以下功能:
     * - 目标选择 -> 添加所有列表中的目标
     * - 目标筛选 -> 删除不在列表中的目标
     * - 目标反筛选 -> 删除在列表中的目标
     *
     * @param caster 释放者
     * @return 目标列表
     */
    abstract fun SelectorContext.getTargets(caster: Target): Collection<Target>

    override fun SelectorContext.select(caster: Target) {
        result.addAll(getTargets(caster))
    }

    override fun SelectorContext.filter(caster: Target) {
        val targets = getTargets(caster).toHashSet()
        result.removeIf { it !in targets }
    }

    override fun SelectorContext.except(caster: Target) {
        val targets = getTargets(caster).toHashSet()
        result.removeIf { it in targets }
    }
}