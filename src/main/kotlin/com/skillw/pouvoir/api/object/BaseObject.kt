package com.skillw.pouvoir.api.`object`

import com.skillw.pouvoir.api.able.Keyable
import org.bukkit.configuration.serialization.ConfigurationSerializable

interface BaseObject : Keyable<String>,
    Comparable<BaseObject>, ConfigurationSerializable {
    override val key: String
    val priority: Int
    override fun compareTo(other: BaseObject): Int = if (this.priority <= other.priority) 1 else -1
}