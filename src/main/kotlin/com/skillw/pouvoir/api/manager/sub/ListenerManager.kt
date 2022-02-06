package com.skillw.pouvoir.api.manager.sub

import com.skillw.pouvoir.api.listener.ScriptListener
import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.map.BaseMap

abstract class ListenerManager : BaseMap<String, ScriptListener>(), Manager