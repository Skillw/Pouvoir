package com.skillw.pouvoir.api.listener

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.Pouvoir.listenerManager
import com.skillw.pouvoir.api.able.Keyable
import com.skillw.pouvoir.util.MessageUtils.info
import com.skillw.pouvoir.util.MessageUtils.wrong
import org.bukkit.Bukkit
import org.bukkit.event.*
import org.bukkit.plugin.EventExecutor

abstract class ScriptListener(
    override val key: String,
    val eventClass: Class<out Event>,
    val priority: EventPriority
) : Listener,
    Keyable<String> {
    private var exec: EventExecutor? = null
    private var handler: HandlerList? = null

    companion object {
        fun build(
            key: String,
            event: Class<out Event>,
            eventPriority: EventPriority = EventPriority.NORMAL,
            ignoreCancel: Boolean = false,
            exec: (Event) -> Unit
        ): ScriptListener {
            val obj = object : ScriptListener(key, event, eventPriority) {
                override fun exec(event: Event) {
                    val cancel = if (event is Cancellable) !event.isCancelled || ignoreCancel else true
                    if (cancel && event.javaClass.name == this.eventClass.name) {
                        exec.invoke(event)
                    }
                }
            }
            return obj
        }
    }

    @EventHandler
    open fun exec(event: Event) {
        info("Event: ${event.eventName}")
    }

    fun unRegister() {
        if (handler == null || exec == null) {
            wrong("Wrong! Please check the messages before this one in console!")
            return
        }
        if (listenerManager.containsKey(key)) {
            handler!!.unregister(listenerManager[key]!!)
        }
    }

    override fun register() {
        if (handler == null || exec == null) {
            wrong("Wrong! Please check the messages before this one in console!")
            return
        }
        val pluginManager = Bukkit.getPluginManager()
        val priority = EventPriority.NORMAL
        if (listenerManager.containsKey(key)) {
            handler!!.unregister(listenerManager[key]!!)
        }
        pluginManager.registerEvent(eventClass, this, priority, exec!!, Pouvoir.plugin)
        listenerManager[key] = this
    }

    init {
        exec = EventExecutor { _: Listener, eventObject: Event -> exec(eventObject) }
        var handler: HandlerList? = null
        try {
            handler = eventClass.getMethod("getHandlerList").invoke(null) as HandlerList
        } catch (e: Exception) {
        }
        if (handler == null) {
            wrong("The class &6${eventClass.name} &cisn't a Event! &7(Haven't the handler)")
        }
        this.handler = handler
    }
}