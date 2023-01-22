package com.skillw.pouvoir.internal.feature.trigger.bukkit

import com.skillw.pouvoir.api.feature.trigger.BukkitTrigger
import com.skillw.pouvoir.api.plugin.handler.ClassHandler
import com.skillw.pouvoir.util.plugin.PluginUtils
import com.skillw.pouvoir.util.safe
import org.bukkit.event.Event
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.library.reflex.ClassStructure

object EventNameHandler : ClassHandler(0) {
    val keyToEvent = HashMap<String, Class<*>>()

    @Awake(LifeCycle.LOAD)
    fun foreachAllEvent() {
        safe {
            PluginUtils.getClasses("org.bukkit.event")
                .forEach(EventNameHandler::addEventNameFormat)
        }
    }

    fun addEventNameFormat(clazz: Class<*>) {
        if (!clazz.isBukkitEvent()) return
        if (clazz.isAnnotationPresent(BukkitTrigger::class.java)) {
            val name = clazz.getAnnotation(BukkitTrigger::class.java).name.ifEmpty { clazz.simpleName.format() }
            keyToEvent += name to clazz
            return
        }
        keyToEvent += clazz.simpleName.format() to clazz
    }

    override fun inject(clazz: ClassStructure) {
        if (!clazz.owner.isBukkitEvent()) return
        addEventNameFormat(clazz.owner)
    }

    internal fun Class<*>.isBukkitEvent(): Boolean {
        return Event::class.java.isAssignableFrom(this) && simpleName != "Event"
    }

    private fun String.format(): String {
        return replace("Event", "")
            .replace(Regex("([A-Z])"), " $1")
            .substring(1)
            .lowercase()
    }
}
