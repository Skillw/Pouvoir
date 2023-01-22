package com.skillw.pouvoir.internal.feature.trigger.custom

import com.skillw.pouvoir.api.feature.trigger.TriggerController
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import java.util.concurrent.ConcurrentHashMap

/**
 * CustomTriggerController
 *
 * 依托答辩
 *
 * @constructor Create empty Bukkit trigger controller
 */
@AutoRegister
object CustomTriggerController : TriggerController<CustomTrigger>("custom", CustomTrigger::class.java) {
    private val tasks = ConcurrentHashMap<String, LinkedHashSet<CustomTriggerHandler>>()
    override fun call(trigger: CustomTrigger) {
        tasks[trigger.key]?.forEach { task ->
            task.handle(trigger)
        }
    }

    override fun onAddTask(triggerKey: String, key: String, priority: Int, run: (CustomTrigger) -> Unit) {
        tasks.computeIfAbsent(triggerKey) { LinkedHashSet() }.apply {
            add(CustomTriggerHandler(key, priority, run))
            sorted()
        }
    }

    override fun onRemoveTrigger(triggerKey: String) {
        tasks[triggerKey]?.apply {
            filter { it.key == key }
                .forEach {
                    remove(it)
                }
        }
    }

    override fun onRemoveTask(triggerKey: String, key: String) {
        tasks[triggerKey]?.removeIf { it.key == key }
    }

}