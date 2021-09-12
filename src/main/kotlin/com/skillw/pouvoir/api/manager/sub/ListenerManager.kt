package com.skillw.pouvoir.api.manager.sub

import com.skillw.pouvoir.api.listener.ScriptListener
import com.skillw.pouvoir.api.manager.Manager
import com.skillw.rpglib.api.map.BaseMap

object ListenerManager : BaseMap<String, ScriptListener>(), Manager {
    override val priority = 4
    override fun removeByKey(k: String) {
        if (hasKey(k)) {
            get(k)!!.unRegister()
        }
        super.removeByKey(k)
    }

    override val key = "ListenerManager"
}