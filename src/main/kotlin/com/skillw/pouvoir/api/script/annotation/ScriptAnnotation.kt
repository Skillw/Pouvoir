package com.skillw.pouvoir.api.script.annotation

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.able.Registrable

/**
 * Script annotation
 *
 * @constructor Create empty Script annotation
 * @property key
 * @property awakeWhenEnable
 */
abstract class ScriptAnnotation(
    override val key: String,
    val awakeWhenEnable: Boolean = false,
) : Registrable<String> {
    override fun register() {
        Pouvoir.scriptAnnotationManager.register(this)
    }

    /**
     * Handle script annotation
     *
     * @param data
     */
    abstract fun handle(data: ScriptAnnotationData)
}