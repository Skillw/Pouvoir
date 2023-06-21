package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.manager.sub.script.ScriptAnnotationManager

internal object ScriptAnnotationManagerImpl : ScriptAnnotationManager() {
    override val key = "ScriptAnnotationManager"
    override val priority = 4
    override val subPouvoir = Pouvoir
}