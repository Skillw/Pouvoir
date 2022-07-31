package com.skillw.pouvoir.api.manager.sub

import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.map.BaseMap
import com.skillw.pouvoir.api.placeholder.PouPlaceHolder

/**
 * Pou place holder manager
 *
 * @constructor Create empty Pou place holder manager
 */
abstract class PouPlaceHolderManager : BaseMap<String, PouPlaceHolder>(), Manager