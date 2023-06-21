package com.skillw.pouvoir.internal.feature.dispatcher

import com.skillw.asahi.api.member.context.AsahiContext
import com.skillw.pouvoir.api.feature.handler.BaseDispatcher
import com.skillw.pouvoir.api.feature.trigger.BaseTrigger
import com.skillw.pouvoir.api.plugin.`object`.Releasable

/**
 * @className SimpleDispatcher
 *
 * @author Glom
 * @date 2023/1/22 20:15 Copyright 2023 user. All rights reserved.
 */
class SimpleDispatcher(
    key: String,
    vararg triggers: String,
    priority: Int,
    receiver: SimpleDispatcher.() -> Unit = {},
) :
    BaseDispatcher(key, *triggers, priority = priority), Releasable {
    private var pre: ((BaseTrigger, AsahiContext) -> Unit)? = null
    private var post: ((BaseTrigger, AsahiContext) -> Unit)? = null
    private var exception: ((BaseTrigger, AsahiContext) -> Unit)? = null

    override var release = false

    init {
        this.receiver()
    }

    fun preHandle(pre: ((BaseTrigger, AsahiContext) -> Unit)?): SimpleDispatcher {
        this.pre = pre
        return this
    }

    fun postHandle(post: ((BaseTrigger, AsahiContext) -> Unit)?): SimpleDispatcher {
        this.post = post
        return this
    }

    fun exception(exception: ((BaseTrigger, AsahiContext) -> Unit)?): SimpleDispatcher {
        this.exception = exception
        return this
    }

    override fun preHandle(trigger: BaseTrigger, context: AsahiContext) {
        pre?.let { it(trigger, context) }
    }

    override fun postHandle(trigger: BaseTrigger, context: AsahiContext) {
        post?.let { it(trigger, context) }
    }

    override fun exception(trigger: BaseTrigger, context: AsahiContext) {
        exception?.let { it(trigger, context) }
    }
}