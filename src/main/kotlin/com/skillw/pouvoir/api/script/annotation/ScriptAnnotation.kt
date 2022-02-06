package com.skillw.pouvoir.api.script.annotation

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.able.Keyable

abstract class ScriptAnnotation(
    override val key: String,
    private val handle: (ScriptAnnotationData) -> Unit,
    val awakeWhenEnable: Boolean = false
) : Keyable<String> {
    override fun register() {
        Pouvoir.scriptAnnotationManager.register(this)
    }

    fun handle(data: ScriptAnnotationData) {
        handle.invoke(data)
    }
}