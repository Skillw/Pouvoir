package com.skillw.pouvoir.internal.script.common.top

import com.skillw.pouvoir.api.able.Registrable

internal data class TopLevelData(
    override val key: String,
    val source: String,
    val type: TopLevel.Type,
    val member: Any,
    val info: String,
) : Registrable<String> {

    override fun register() {
        TopLevel.register(key, this)
    }
}
