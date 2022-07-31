package com.skillw.pouvoir.api.placeholder

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.able.Registrable
import com.skillw.pouvoir.api.plugin.SubPouvoir
import org.bukkit.entity.LivingEntity

/**
 * ClassName : com.skillw.pouvoir.api.analysis.PouPlaceHolder
 * Created by Glom_ on 2021-03-28 15:40:22
 * Copyright  2021 user. All rights reserved.
 */
abstract class PouPlaceHolder(
    identifier: String,
    val name: String,
    val author: String,
    val version: String,
) : Registrable<String> {

    constructor(identifier: String, subPouvoir: SubPouvoir) : this(
        identifier,
        subPouvoir.plugin.name,
        subPouvoir.plugin.description.authors.toString(),
        subPouvoir.plugin.description.version
    )

    final override val key: String = identifier
    abstract fun onPlaceHolderRequest(params: String, entity: LivingEntity, def: String): String?
    override fun register() {
        Pouvoir.pouPlaceholderManager.register(key, this)
    }
}