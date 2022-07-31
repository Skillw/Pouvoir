package com.skillw.pouvoir.api.event

import taboolib.platform.type.BukkitProxyEvent

/**
 * @className PouManagerEvent
 * @author Glom
 * @date 2022/7/22 16:13
 * Copyright  2022 user. All rights reserved.
 */
class PouManagerEvent(val manager: String, val time: ManagerTime) : BukkitProxyEvent()