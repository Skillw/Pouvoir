package com.skillw.pouvoir.internal.feature.trigger.custom

import com.skillw.pouvoir.api.feature.handler.BaseHandler

/**
 * @className CustomTriggerHandler
 *
 * @author Glom
 * @date 2023/1/22 20:01 Copyright 2023 user. All rights reserved.
 */
class CustomTriggerHandler(key: String, priority: Int, val todo: (CustomTrigger) -> Unit) :
    BaseHandler<CustomTrigger>(key, priority = priority) {
    override fun handle(trigger: CustomTrigger) {
        todo(trigger)
    }

}