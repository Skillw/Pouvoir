package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.api.listener.ScriptListener
import com.skillw.pouvoir.api.manager.sub.ListenerManager

object ListenerManagerImpl : ListenerManager() {
    override val key = "ListenerManager"
    override val priority = 3


    override fun remove(key: String): ScriptListener? {
        if (hasKey(key)) {
            get(key)!!.unRegister()
        }
        return super.remove(key)
    }

}