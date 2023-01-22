package com.skillw.pouvoir.internal.feature.dispatcher

import com.skillw.asahi.api.member.context.AsahiContext
import com.skillw.asahi.internal.util.Time
import com.skillw.asahi.util.castSafely
import com.skillw.asahi.util.toLazyMap
import com.skillw.pouvoir.api.feature.trigger.BaseTrigger
import com.skillw.pouvoir.api.plugin.map.DataMap
import com.skillw.pouvoir.internal.feature.handler.toInvoker
import com.skillw.pouvoir.internal.feature.trigger.loadIn
import com.skillw.pouvoir.util.toMap
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.entity.Player
import taboolib.common.platform.event.EventPriority
import taboolib.common5.Baffle
import taboolib.common5.cint
import java.util.concurrent.TimeUnit

/**
 * @className AsahiHandler
 *
 * @author Glom
 * @date 2023/1/15 23:12 Copyright 2023 user. All rights reserved.
 */
class SimpleDispatcherBuilder(
    initial: AsahiContext = AsahiContext.create(),
    receiver: SimpleDispatcherBuilder.() -> Unit = {},
) : AsahiContext by initial,
    ConfigurationSerializable {
    var key = ""
    val triggers = HashSet<String>()
    var priority = EventPriority.NORMAL
    val import = HashSet<String>()
    val namespaces = HashSet<String>()
    var baffle: Baffle? = null
    var baffleBaseOnPlayer = false
    private var pre: ((BaseTrigger, AsahiContext) -> Unit)? = null
    private var post: ((BaseTrigger, AsahiContext) -> Unit)? = null
    private var exception: ((BaseTrigger, AsahiContext) -> Unit)? = null
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

        if (data.containsKey("pre-handle")) {
            kotlin.run {
                val value = data.get("pre-handle")!!.toInvoker("pre", namespaces) ?: return@run
                preHandle { trigger, context ->
                    context.loadIn(trigger)
                    value.invoke(context)
                }
            }
        }

        if (data.containsKey("post-handle")) {
            kotlin.run {
                val value = data.get("post-handle")!!.toInvoker("post", namespaces) ?: return@run
                postHandle { trigger, context ->
                    context.loadIn(trigger)
                    value.invoke(context)
                }
            }
        }

        if (data.containsKey("exception")) {
            kotlin.run {
                val value = data.get("exception")!!.toInvoker("exception", namespaces) ?: return@run
                exception { trigger, context ->
                    context.loadIn(trigger)
                    value.invoke(context)
                }
            }
        }
        if (data.containsKey("baffle")) {
            val baffleData = data["baffle"] as Map<String, Any>
            baffleBaseOnPlayer = baffleData["based-on"] == "player"
            baffle = when {
                baffleData.containsKey("counter") -> {
                    val count = baffleData["count"].cint
                    Baffle.of(count)
                }

                baffleData.containsKey("time") -> {
                    val time = Time(baffleData["time"].toString()).millis
                    Baffle.of(time, TimeUnit.MILLISECONDS)
                }

                else -> {
                    taboolib.common.platform.function.warning("Wrong Baffle Data in Dispatcher $key's Config")
                    null
                }
            }

        }
        data.get("functions", emptyMap<String, Any>()).forEach { (key, value) ->
            invokers[key] = value.toInvoker(key, namespaces) ?: return@forEach
        }
        release = true
    }

    fun preHandle(pre: ((BaseTrigger, AsahiContext) -> Unit)?): SimpleDispatcherBuilder {
        this.pre = pre
        return this
    }

    fun postHandle(post: ((BaseTrigger, AsahiContext) -> Unit)?): SimpleDispatcherBuilder {
        this.post = post
        return this
    }

    fun exception(exception: ((BaseTrigger, AsahiContext) -> Unit)?): SimpleDispatcherBuilder {
        this.exception = exception
        return this
    }

    fun baffle(baffle: Baffle?): SimpleDispatcherBuilder {
        this.baffle = baffle
        return this
    }

    fun build(): SimpleDispatcher {
        val builder = this
        return SimpleDispatcher(key, *triggers.toList().toTypedArray(), priority = priority.level) {
            import(*import.toTypedArray())
            preHandle { trigger, context ->
                context.putAll(builder.context())
                context.loadIn(trigger)
                builder.baffle?.let {
                    val playerName = context["player"].castSafely<Player>()?.name ?: "NONE"
                    val key =
                        if (baffleBaseOnPlayer) "${trigger.key}-${builder.key}-$playerName" else "${trigger.key}-${builder.key}"
                    if (!it.hasNext(key)) {
                        context["@exit"] = true
                        exit()
                        return@preHandle
                    }
                }
                builder.pre?.let { it(trigger, context) }
            }
            postHandle(builder.post)
            exception(builder.exception)
            release = builder.release
        }
    }

    override fun serialize(): MutableMap<String, Any> {
        return linkedMapOf()
    }

    companion object {
        @JvmStatic
        fun deserialize(section: org.bukkit.configuration.ConfigurationSection): SimpleDispatcherBuilder {
            val key = section.name
            return SimpleDispatcherBuilder(key, section.toMap())
        }
    }

}
