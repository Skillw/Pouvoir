package com.skillw.pouvoir.internal.feature.trigger.bukkit

import com.skillw.pouvoir.Pouvoir.listenerManager
import com.skillw.pouvoir.api.feature.trigger.TriggerController
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import com.skillw.pouvoir.internal.feature.listener.CustomListener
import com.skillw.pouvoir.internal.feature.listener.Priority
import com.skillw.pouvoir.internal.feature.trigger.bukkit.EventNameHandler.isBukkitEvent
import com.skillw.pouvoir.internal.feature.trigger.bukkit.EventNameHandler.keyToEvent
import com.skillw.pouvoir.util.existClass
import com.skillw.pouvoir.util.findClass
import org.bukkit.event.Event
import taboolib.common.platform.Platform
import java.util.concurrent.ConcurrentHashMap

/**
 * Bukkit trigger controller
 *
 * 依托答辩
 *
 * @constructor Create empty Bukkit trigger controller
 */
@AutoRegister
object BukkitTriggerController : TriggerController<BukkitEventTrigger>("bukkit", BukkitEventTrigger::class.java) {
    private val tasks = ConcurrentHashMap<String, ConcurrentHashMap<String, (BukkitEventTrigger) -> Unit>>()
    private fun listenerKey(triggerKey: String, priority: Int) = "Trigger-$triggerKey-${Priority(priority).toBukkit()}"
    private fun bindToEvent(listenerKey: String, eventClass: Class<*>, priority: Int) {
        if (listenerManager.containsKey(listenerKey)) return
        CustomListener.Builder(
            listenerKey,
            Platform.BUKKIT,
            eventClass,
            Priority(priority).toBukkit(),
            ignoreCancel = true
        ) { event ->
            event as Event
            BukkitEventTrigger(listenerKey, event).call()
        }.build().register()
    }

    override fun call(trigger: BukkitEventTrigger) {
        tasks[trigger.key]?.forEach { (_, task) ->
            task(trigger)
        }
    }

    private fun String.toClazzName() = if (keyToEvent.containsKey(this)) keyToEvent[this]!!.name else this


    override fun onAddTask(triggerKey: String, key: String, priority: Int, run: (BukkitEventTrigger) -> Unit) {
        val clazz: Class<*>
        val trigger = if (keyToEvent.containsKey(triggerKey)) {
            clazz = keyToEvent[triggerKey]!!
            clazz.name
        } else {
            clazz = triggerKey.findClass() ?: error("Can't find Bukkit Event $triggerKey")
            triggerKey
        }
        val listenerKey = listenerKey(trigger, priority)
        bindToEvent(listenerKey, clazz, priority)
        tasks.computeIfAbsent(listenerKey) { ConcurrentHashMap() }[key] = run
    }

    override fun onRemoveTrigger(triggerKey: String) {
        val clazzName = triggerKey.toClazzName()
        listenerManager.keys.filter { it.startsWith("Trigger-$clazzName-") }.forEach(listenerManager::remove)
        tasks.keys.filter { it.startsWith("Trigger-$clazzName-") }.forEach {
            tasks.remove(it)
        }
    }

    override fun predicate(triggerKey: String): Boolean {
        return keyToEvent.containsKey(triggerKey) || (triggerKey.existClass() && (triggerKey.findClass()
            ?.isBukkitEvent() == true))
    }

    override fun onRemoveTask(triggerKey: String, key: String) {
        val clazzName = triggerKey.toClazzName()
        tasks.filter { (key, _) ->
            key.startsWith("Trigger-$clazzName")
        }.forEach { (_, map) ->
            map.remove(key)
        }
    }

}