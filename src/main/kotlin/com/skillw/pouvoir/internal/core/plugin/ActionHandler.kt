package com.skillw.pouvoir.internal.core.plugin

import com.skillw.pouvoir.api.plugin.handler.ClassHandler
import com.skillw.pouvoir.api.function.action.IAction
import org.bukkit.plugin.Plugin

object ActionHandler : ClassHandler(2) {
    override fun inject(clazz: Class<*>, plugin: Plugin) {
        if (IAction::class.java.isAssignableFrom(clazz) && clazz.simpleName != "IAction") {
            IAction.register(clazz.getField("INSTANCE").get(null) as IAction)
        }
    }
}
