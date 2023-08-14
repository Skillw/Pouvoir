package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.manager.sub.OperationManager

object OperationManagerImpl : OperationManager() {
    override val key = "OperationManager"
    override val priority: Int = 0
    override val subPouvoir = Pouvoir

    override fun onEnable() {
        onReload()
    }

    override fun onReload() {
        this.entries.filter { it.value.release }.forEach { this.remove(it.key) }
    }

}

