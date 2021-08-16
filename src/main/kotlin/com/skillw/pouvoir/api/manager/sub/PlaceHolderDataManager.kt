package com.skillw.pouvoir.api.manager.sub

import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.placeholder.RPGPlaceHolder
import com.skillw.rpglib.api.map.BaseMap

object PlaceHolderDataManager : BaseMap<String, RPGPlaceHolder>(), Manager {
    override val key = "PlaceHolderDataManager"
}