package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.manager.sub.ListenerManager
import taboolib.common.platform.event.ProxyListener
import taboolib.common.platform.function.unregisterListener

internal object ListenerManagerImpl : ListenerManager() {
    override val key = "ListenerManager"
    override val priority = 3
    override val subPouvoir = Pouvoir

    override fun remove(key: String): ProxyListener? {
        get(key)?.let { unregisterListener(it) }
        return super.remove(key)
    }

    override fun onReload() {
        forEach {
            remove(it.key)
        }
    }

}