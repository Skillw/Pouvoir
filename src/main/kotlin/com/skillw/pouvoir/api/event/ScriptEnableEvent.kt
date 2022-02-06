package com.skillw.pouvoir.api.event

import taboolib.common.platform.event.ProxyEvent

class ScriptEnableEvent(val time: Time) : ProxyEvent() {
    override val allowCancelled = false
}