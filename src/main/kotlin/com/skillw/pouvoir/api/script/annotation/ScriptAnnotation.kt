package com.skillw.pouvoir.api.script.annotation

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.able.Registrable

abstract class ScriptAnnotation(
    override val key: String,
    val awakeWhenEnable: Boolean = false
) : Registrable<String> {
    override fun register() {
        Pouvoir.scriptAnnotationManager.register(this)
    }

    abstract fun handle(data: ScriptAnnotationData)
}