package com.skillw.pouvoir.internal.feature.handler

import com.skillw.asahi.api.member.context.AsahiContext
import com.skillw.pouvoir.api.feature.handler.BaseHandler
import com.skillw.pouvoir.api.feature.trigger.BaseTrigger
import com.skillw.pouvoir.api.plugin.`object`.Releasable
import com.skillw.pouvoir.internal.feature.trigger.loadIn

/**
 * @className AsahiHandler
 *
 * @author Glom
 * @date 2023/1/15 23:12 Copyright 2023 user. All rights reserved.
 */
class AsahiHandler internal constructor(
    key: String,
    vararg triggers: String,
    priority: Int = 3,
    initial: AsahiContext.() -> Unit = {},
) :
    BaseHandler<BaseTrigger>(key, *triggers, priority = priority),
    AsahiContext by AsahiContext.create().apply(initial), Releasable {
    private var handle: (AsahiContext.() -> Unit)? = null
    override var release = false

    fun handle(handle: AsahiContext.() -> Unit): AsahiHandler {
        this.handle = handle
        return this
    }

    override fun handle(trigger: BaseTrigger) {
        context().clone().apply {
            loadIn(trigger)
            handle?.let { it() }
        }
    }

}