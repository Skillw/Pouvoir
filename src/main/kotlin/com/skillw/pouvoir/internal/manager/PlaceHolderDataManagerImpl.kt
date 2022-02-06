package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.api.manager.sub.PlaceHolderDataManager

object PlaceHolderDataManagerImpl : PlaceHolderDataManager() {
    override val key = "PlaceHolderDataManager"
    override val priority = 2
}