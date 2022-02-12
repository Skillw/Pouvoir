package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.manager.sub.PouPlaceHolderManager
import com.skillw.pouvoir.api.placeholder.PouPlaceHolder
import com.skillw.pouvoir.api.plugin.TotalManager
import com.skillw.pouvoir.internal.handle.PouPlaceHolderHandle
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang

object PouPlaceHolderManagerImpl : PouPlaceHolderManager() {
    override val key = "PouPlaceHolderManager"
    override val priority = 2
    override val subPouvoir = Pouvoir

    override fun onEnable() {
        TotalManager.forEachClass {
            PouPlaceHolderHandle.inject(it)
        }
    }

    override fun register(key: String, value: PouPlaceHolder) {
        super.register(key, value)
        val description = value.subPouvoir.plugin.description
        console().sendLang(
            "pou-placeholder-register",
            key,
            description.name,
            description.version,
            description.authors.toString()
        )
    }
}