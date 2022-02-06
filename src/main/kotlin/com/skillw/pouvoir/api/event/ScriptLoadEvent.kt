package com.skillw.pouvoir.api.event

import taboolib.common.platform.event.ProxyEvent

class ScriptLoadEvent(val time: Time) : ProxyEvent() {
    override val allowCancelled = false
}