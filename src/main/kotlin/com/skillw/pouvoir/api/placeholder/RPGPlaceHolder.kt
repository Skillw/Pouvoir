package com.skillw.pouvoir.api.placeholder

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.able.Keyable
import org.bukkit.entity.LivingEntity

/**
 * ClassName : com.skillw.com.skillw.rpglib.api.analysis.RPGPlaceHolder
 * Created by Glom_ on 2021-03-28 15:40:22
 * Copyright  2021 user. All rights reserved.
 */
abstract class RPGPlaceHolder(identifier: String, author: String?, version: String?) : Keyable<String> {
    final override val key: String
    var author: String? = null
    var version: String? = null
    abstract fun onPlaceHolderRequest(params: String, livingEntity: LivingEntity, def: String): String?
    override fun register() {
        Pouvoir.placeholderDataManager.register(key, this)
    }

    init {
        this.author = author
        key = identifier
        this.version = version
    }
}