package com.skillw.pouvoir.internal.feature.trigger.custom

import com.skillw.pouvoir.api.feature.trigger.BaseTrigger
import com.skillw.pouvoir.api.plugin.map.DataMap

/**
 * @className CustomTrigger
 *
 * @author Glom
 * @date 2023/1/8 19:36 Copyright 2024 Glom.
 */
class CustomTrigger(key: String) : BaseTrigger(key) {
    val data = DataMap()

    constructor(key: String, data: Map<String, Any> = emptyMap()) : this(key) {
        this.data.putAll(data)
    }

    constructor(key: String, receiver: DataMap.() -> Unit) : this(key) {
        receiver(data)
    }
}