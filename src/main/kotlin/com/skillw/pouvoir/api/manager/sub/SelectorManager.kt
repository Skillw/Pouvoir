package com.skillw.pouvoir.api.manager.sub

import com.skillw.pouvoir.api.feature.selector.BaseSelector
import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.plugin.map.LowerKeyMap

/** BaseSelector */
abstract class SelectorManager : LowerKeyMap<BaseSelector>(), Manager