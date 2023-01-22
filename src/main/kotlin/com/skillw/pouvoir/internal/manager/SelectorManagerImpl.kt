package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.manager.sub.SelectorManager

internal object SelectorManagerImpl : SelectorManager() {
    override val key = "SelectorManager"
    override val priority = 5
    override val subPouvoir = Pouvoir

}