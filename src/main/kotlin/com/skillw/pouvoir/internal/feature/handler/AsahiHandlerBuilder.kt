package com.skillw.pouvoir.internal.feature.handler

import com.skillw.asahi.api.AsahiAPI.compile
import com.skillw.asahi.api.member.context.AsahiContext
import com.skillw.asahi.api.script.AsahiCompiledScript
import com.skillw.asahi.util.castSafely
import com.skillw.asahi.util.toLazyMap
import com.skillw.pouvoir.api.plugin.map.DataMap
import com.skillw.pouvoir.util.toMap
import org.bukkit.configuration.serialization.ConfigurationSerializable
import taboolib.common.platform.event.EventPriority
import taboolib.common5.cbool
import java.util.*

/**
 * @className AsahiHandler
 *
 * @author Glom
 * @date 2023/1/15 23:12 Copyright 2023 user. All rights reserved.
 */
class AsahiHandlerBuilder(
    initial: AsahiContext = AsahiContext.create(),
    receiver: AsahiHandlerBuilder.() -> Unit = {},
) : AsahiContext by initial,
    ConfigurationSerializable {
    var key = ""
    val triggers = HashSet<String>()
    var priority = EventPriority.NORMAL
    val import = HashSet<String>()
    val control = LinkedList<Map<String, Any>>()
    val namespaces = HashSet<String>()
    var release = false

    init {
        this.receiver()
    }

    constructor(key: String, map: Map<String, Any> = emptyMap()) : this() {
        val data = DataMap().apply { putAll(map) }
        this.key = key
        priority = data.get("priority", "NORMAL").castSafely<EventPriority>() ?: EventPriority.NORMAL
        triggers.addAll(data.get("triggers", emptyList()))
        namespaces.addAll(data.get("namespaces", listOf("common", "lang")))
        import.addAll(data.get("import", emptyList()))
        context().putAll(data.get("context", emptyMap<String, Any>()).toLazyMap())
        control.addAll(data.get("when", emptyList<Map<String, Any>>()).map {
            it.mapValues { entry ->
                if (entry.key == "if") entry.value.toString()
                    .compile(*namespaces.toTypedArray()) else entry.value
            }
        })
        data.get("functions", emptyMap<String, Any>()).forEach { (key, value) ->
            invokers[key] = value.toInvoker(key, namespaces) ?: return@forEach
        }
        release = true
    }

    fun build(): AsahiHandler {
        return AsahiHandler(key, *triggers.toList().toTypedArray(), priority = priority.level) {
            import(*import.toTypedArray())
            putAll(this@AsahiHandlerBuilder.context())
        }.apply {
            release = this@AsahiHandlerBuilder.release
        }.handle {

            val goto = control.firstOrNull { (it["if"] as? AsahiCompiledScript?)?.run().cbool }?.get("goto")
                ?: control.lastOrNull()?.get("else") ?: "main"
            invokers[goto]!!.invoke(this)
        }
    }

    override fun serialize(): MutableMap<String, Any> {
        return linkedMapOf()
    }

    companion object {
        @JvmStatic
        fun deserialize(section: org.bukkit.configuration.ConfigurationSection): AsahiHandlerBuilder {
            val key = section.name
            return AsahiHandlerBuilder(key, section.toMap())
        }
    }

}
