package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.feature.trigger.BaseTrigger
import com.skillw.pouvoir.api.feature.trigger.TriggerController
import com.skillw.pouvoir.api.manager.sub.TriggerManager
import com.skillw.pouvoir.api.plugin.map.BaseMap
import com.skillw.pouvoir.api.plugin.map.KeyMap
import com.skillw.pouvoir.internal.feature.trigger.custom.CustomTriggerController

internal object TriggerManagerImpl : TriggerManager() {
    override val key = "TriggerManager"
    override val priority = 5
    override val subPouvoir = Pouvoir
    private val controllers = KeyMap<String, TriggerController<*>>()
    private val controllersByType = BaseMap<Class<*>, TriggerController<*>>()
    override fun <T : BaseTrigger> call(trigger: T) {
        (controllersByType[trigger::class.java] as? TriggerController<BaseTrigger>?)?.call(trigger)
    }

    override fun <T : BaseTrigger> addTask(triggerKey: String, key: String, priority: Int, run: (T) -> Unit) {
        onAddTask(triggerKey, key, priority, run)
    }

    override fun remove(triggerKey: String, key: String) {
        onRemoveTask(triggerKey, key)
    }

    override fun removeAllTask(triggerKey: String) {
        onRemoveTrigger(triggerKey)
    }

    override fun registerController(controller: TriggerController<*>) {
        controllers.register(controller)
        controllersByType.register(controller.triggerClass, controller)
    }

    private fun <T : BaseTrigger> controller(triggerKey: String): Pair<String, TriggerController<T>> {
        val controller: TriggerController<*> =
            controllers.values.firstOrNull { it.predicate(triggerKey) } ?: CustomTriggerController
        return triggerKey to (controller as TriggerController<T>)
    }

    private fun <T : BaseTrigger> onAddTask(triggerKey: String, key: String, priority: Int, run: (T) -> Unit) {
        val (trigger, controller) = controller<T>(triggerKey)
        controller.onRemoveTask(trigger, key)
        controller.onAddTask(trigger, key, priority, run)
    }

    private fun onRemoveTrigger(triggerKey: String) {
        val (trigger, controller) = controller<BaseTrigger>(triggerKey)
        controller.onRemoveTrigger(trigger)
    }

    private fun onRemoveTask(triggerKey: String, key: String) {
        val (trigger, controller) = controller<BaseTrigger>(triggerKey)
        controller.onRemoveTask(trigger, key)
    }
}