package com.skillw.pouvoir.api.manager.sub

import com.skillw.pouvoir.api.listener.ScriptListener
import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.map.BaseMap

object ListenerManager : BaseMap<String, ScriptListener>(), Manager {
    override val priority = 4
    override fun removeByKey(key: String) {
        if (hasKey(key)) {
            get(key)!!.unRegister()
        }
        super.removeByKey(key)
    }

    override val key = "ListenerManager"
}