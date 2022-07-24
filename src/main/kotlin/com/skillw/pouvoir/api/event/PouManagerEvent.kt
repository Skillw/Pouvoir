package com.skillw.pouvoir.api.event

import com.skillw.pouvoir.api.manager.Manager
import taboolib.platform.type.BukkitProxyEvent

/**
 * @className PouManagerEvent
 * @author Glom
 * @date 2022/7/22 16:13
 * Copyright  2022 user. All rights reserved.
 */
class PouManagerEvent(val manager: Manager, val time: ManagerTime) : BukkitProxyEvent()