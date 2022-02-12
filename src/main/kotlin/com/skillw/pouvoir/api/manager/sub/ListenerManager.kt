package com.skillw.pouvoir.api.manager.sub

import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.map.LowerMap
import taboolib.common.platform.event.ProxyListener

abstract class ListenerManager : LowerMap<ProxyListener>(), Manager