package com.skillw.pouvoir.internal.core.asahi

import com.skillw.asahi.internal.AsahiLoader
import com.skillw.pouvoir.api.plugin.handler.ClassHandler
import taboolib.library.reflex.ClassStructure

object AsahiInject : ClassHandler(0) {
    override fun inject(clazz: ClassStructure) {
        AsahiLoader.inject(clazz)
    }
}