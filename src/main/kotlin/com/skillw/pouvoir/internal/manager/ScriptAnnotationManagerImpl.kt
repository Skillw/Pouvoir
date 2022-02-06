package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.api.manager.sub.script.ScriptAnnotationManager
import com.skillw.pouvoir.api.plugin.TotalManager
import com.skillw.pouvoir.internal.handle.ScriptAnnotationHandler

object ScriptAnnotationManagerImpl : ScriptAnnotationManager() {
    override val key = "ScriptAnnotationManager"
    override val priority = 6
    override fun init() {
        TotalManager.forEachClass {
            ScriptAnnotationHandler.inject(it)
        }
    }

}