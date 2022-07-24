package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.manager.sub.script.ScriptAnnotationManager

object ScriptAnnotationManagerImpl : ScriptAnnotationManager() {
    override val key = "ScriptAnnotationManager"
    override val priority = 6
    override val subPouvoir = Pouvoir
}