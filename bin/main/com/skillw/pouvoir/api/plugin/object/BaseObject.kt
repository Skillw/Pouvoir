package com.skillw.pouvoir.api.plugin.`object`

import com.skillw.pouvoir.api.plugin.map.component.Registrable
import org.bukkit.configuration.serialization.ConfigurationSerializable

/**
 * Base object
 *
 * @constructor Create empty Base object
 */
interface BaseObject : Registrable<String>,
    Comparable<BaseObject>,
    ConfigurationSerializable {
    override val key: String

    /** Priority */
    val priority: Int
    override fun compareTo(other: BaseObject): Int = if (this.priority == other.priority) 0
    else if (this.priority > other.priority) 1
    else -1
}