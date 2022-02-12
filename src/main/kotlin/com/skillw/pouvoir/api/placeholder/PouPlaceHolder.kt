package com.skillw.pouvoir.api.placeholder

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.able.Keyable
import com.skillw.pouvoir.api.plugin.SubPouvoir
import org.bukkit.entity.LivingEntity

/**
 * ClassName : com.skillw.com.skillw.rpglib.api.analysis.PouPlaceHolder
 * Created by Glom_ on 2021-03-28 15:40:22
 * Copyright  2021 user. All rights reserved.
 */
abstract class PouPlaceHolder(
    identifier: String,
    val subPouvoir: SubPouvoir
) : Keyable<String> {
    final override val key: String = identifier
    abstract fun onPlaceHolderRequest(params: String, livingEntity: LivingEntity, def: String): String?
    override fun register() {
        Pouvoir.pouPlaceholderManager.register(key, this)
    }
}