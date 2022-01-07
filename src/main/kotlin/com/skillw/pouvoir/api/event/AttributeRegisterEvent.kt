package com.skillw.pouvoir.api.event

import com.skillw.pouvoir.api.attribute.BaseAttribute
import taboolib.common.platform.event.ProxyEvent


class AttributeRegisterEvent(val time: Time, val baseAttribute: BaseAttribute) : ProxyEvent() {
}