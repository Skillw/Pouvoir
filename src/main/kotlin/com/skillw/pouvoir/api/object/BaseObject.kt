package com.skillw.pouvoir.api.`object`

import com.skillw.pouvoir.api.able.Registrable
import org.bukkit.configuration.serialization.ConfigurationSerializable

interface BaseObject : Registrable<String>,
    Comparable<BaseObject>,
    ConfigurationSerializable {
    override val key: String
    val priority: Int
    override fun compareTo(other: BaseObject): Int = if (this.priority == other.priority) 0
    else if (this.priority > other.priority) 1
    else -1
}