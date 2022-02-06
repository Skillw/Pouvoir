package com.skillw.pouvoir.api.event

import taboolib.common.platform.event.ProxyEvent

class ScriptDisableEvent(val time: Time) : ProxyEvent() {
    override val allowCancelled = false
}