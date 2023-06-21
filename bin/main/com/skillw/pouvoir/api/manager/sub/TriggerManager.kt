package com.skillw.pouvoir.api.manager.sub

import com.skillw.pouvoir.api.feature.trigger.BaseTrigger
import com.skillw.pouvoir.api.feature.trigger.TriggerController
import com.skillw.pouvoir.api.manager.Manager

/**
 * TriggerManager
 *
 * 触发器管理器
 *
 * 负责维护TriggerController
 *
 * 方法中triggerKey参数 格式为"{控制器id} 触发器id"
 *
 * 调用此管理器中的方法后，管理器会寻找相应TriggerController中的方法并调用
 */

abstract class TriggerManager : Manager {
    /**
     * 触发触发器
     *
     * @param trigger 触发器
     * @param T 类型
     */
    abstract fun <T : BaseTrigger> call(trigger: T)

    /**
     * 添加触发器监听任务
     *
     * @param triggerKey 触发器id
     * @param key 任务id
     * @param priority 优先级
     * @param run 执行内容
     * @param T 触发器类型
     * @receiver
     */
    abstract fun <T : BaseTrigger> addTask(triggerKey: String, key: String, priority: Int = 0, run: (T) -> Unit)

    /**
     * 删除触发器监听任务
     *
     * @param triggerKey 触发器id
     * @param key 任务id
     */
    abstract fun remove(triggerKey: String, key: String)

    /**
     * 注销触发器的所有监听任务
     *
     * @param triggerKey 触发器id
     */
    abstract fun removeAllTask(triggerKey: String)

    /**
     * 注册触发器控制器
     *
     * @param controller 触发器控制器
     */
    abstract fun registerController(controller: TriggerController<*>)
}