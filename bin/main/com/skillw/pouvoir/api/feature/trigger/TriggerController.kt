package com.skillw.pouvoir.api.feature.trigger

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.plugin.map.component.Registrable

/**
 * @className TriggerController
 *
 * 触发器控制器
 *
 * 你需要使用 [ 名称 触发器类T ] 来实例化此类 (记得@AutoRegister)
 *
 * 实例化并注册后，此类将负责T类及其子类的以下处理：
 * - 处理触发器的触发
 * - 处理触发器监听任务
 * - 处理触发器的注销
 * - 处理触发器监听任务的注销
 * - 筛选触发器
 *
 * @author Glom
 * @date 2023/1/21 17:14 Copyright 2023 user. All rights reserved.
 */
abstract class TriggerController<T : BaseTrigger>(override val key: String, val triggerClass: Class<T>) :
    Registrable<String> {

    /**
     * 触发此触发器类及其子类时调用此函数
     *
     * @param trigger 触发器
     */
    open fun call(trigger: T) {}

    /**
     * 此触发器类及其子类新增监听任务时调用此函数
     *
     * @param triggerKey 触发器id
     * @param key 任务id
     * @param priority 优先级
     * @param run 任务内容
     * @receiver
     */
    open fun onAddTask(triggerKey: String, key: String, priority: Int, run: (T) -> Unit) {}

    /**
     * 注销触发器
     *
     * @param triggerKey 触发器id
     */
    open fun onRemoveTrigger(triggerKey: String) {}

    /**
     * 注销触发器的监听任务
     *
     * @param triggerKey 触发器id
     * @param key 任务id
     */
    open fun onRemoveTask(triggerKey: String, key: String) {}

    /**
     * 检查此触发器id是否应归为此控制器管理
     *
     * @param triggerKey 触发器id
     * @return 是否归此控制器管理
     */
    open fun predicate(triggerKey: String): Boolean = false

    override fun register() {
        Pouvoir.triggerManager.registerController(this)
    }
}