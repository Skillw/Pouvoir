package com.skillw.pouvoir.api.script.annotation

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.able.Keyable

abstract class ScriptAnnotation(
    override val key: String,
    val awakeWhenEnable: Boolean = false
) : Keyable<String> {
    override fun register() {
        Pouvoir.scriptAnnotationManager.register(this)
    }

    abstract fun handle(data: ScriptAnnotationData)
}