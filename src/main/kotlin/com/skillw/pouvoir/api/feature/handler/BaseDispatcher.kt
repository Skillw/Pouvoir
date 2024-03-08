package com.skillw.pouvoir.api.feature.handler

import com.skillw.asahi.api.member.context.AsahiContext
import com.skillw.pouvoir.api.feature.trigger.BaseTrigger
import com.skillw.pouvoir.internal.feature.trigger.custom.CustomTrigger
import com.skillw.pouvoir.internal.feature.trigger.loadIn

/**
 * @className BaseDispatcher
 *
 * 基于CustomTriggerController的调度器
 *
 * 基础调度器
 * - key = 触发器id
 * - 自身 = 一个BaseHandler
 *
 * 主要负责拓展触发器，有以下功能&特性
 * - 绑定其它触发器(带优先级)
 * - 可传递的上下文
 * - 预处理 被其它处理器处理 尾处理 异常处理
 *
 * @author Glom
 * @date 2023/1/15 23:02 Copyright 2024 Glom.
 */
abstract class BaseDispatcher(key: String, vararg triggers: String, priority: Int) :
    BaseHandler<BaseTrigger>(key, *triggers, priority = priority) {

    /**
     * 预处理
     *
     * @param trigger 触发器
     */
    open fun preHandle(trigger: BaseTrigger, context: AsahiContext) {

    }

    /**
     * 尾处理
     *
     * @param trigger 触发器
     */
    open fun postHandle(trigger: BaseTrigger, context: AsahiContext) {

    }

    open fun exception(trigger: BaseTrigger, context: AsahiContext) {

    }

    /**
     * 处理bind的触发器
     *
     * @param trigger bind的触发器
     */
    override fun handle(trigger: BaseTrigger) {
        val context: AsahiContext = if (trigger is CustomTrigger && trigger.data.containsKey("context")) {
            trigger.data.getAs<AsahiContext>("context")!!
        } else AsahiContext.create()
        kotlin.runCatching {
            context.onExit {
                context["@exit"] = true
            }
            context["@exit"] = false
            preHandle(trigger, context)
            if (context.isExit() || context["@exit"] == true) return
            CustomTrigger(key) {
                this.loadIn(trigger)
                this["origin-trigger"] = trigger
                this["context"] = context
            }.call()
            if (context.isExit() || context["@exit"] == true) return
            postHandle(trigger, context)
        }.exceptionOrNull()?.let { throwable ->
            exception(trigger, context)
            throwable.printStackTrace()
        }
    }
}