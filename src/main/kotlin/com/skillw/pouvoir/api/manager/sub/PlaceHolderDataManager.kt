package com.skillw.pouvoir.api.manager.sub

import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.map.BaseMap
import com.skillw.pouvoir.api.placeholder.PouPlaceHolder

abstract class PlaceHolderDataManager : BaseMap<String, PouPlaceHolder>(), Manager {
    override val priority = 3
    override val key = "PlaceHolderDataManager"
}