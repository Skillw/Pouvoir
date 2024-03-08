package com.skillw.asahi.api.member.lexer

import com.skillw.asahi.internal.lexer.AsahiDemandImpl
import com.skillw.asahi.util.AsahiDataMap
import org.bukkit.entity.Entity

/**
 * @className PouDemand
 *
 * @author Glom
 * @date 2023/1/14 12:38 Copyright 2024 Glom.
 */
abstract class AsahiDemand(val entity: Entity? = null) : AsahiDataMap() {
    companion object {
        fun of(string: String): AsahiDemand {
            return AsahiDemandImpl.of(string)
        }
    }

    abstract fun tag(tag: String): Boolean

    abstract fun tagAnyOf(vararg tags: String): Boolean

    abstract fun tagAllOf(vararg tags: String): Boolean

    abstract val args: ArrayList<String>
    abstract val tags: LinkedHashSet<String>
}