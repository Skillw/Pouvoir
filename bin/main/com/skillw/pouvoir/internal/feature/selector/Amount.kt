package com.skillw.pouvoir.internal.feature.selector


import com.skillw.asahi.util.castSafely
import com.skillw.pouvoir.api.feature.selector.BaseSelector
import com.skillw.pouvoir.api.feature.selector.Target
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import com.skillw.pouvoir.api.plugin.map.DataMap

/**
 * Amount
 *
 * @constructor Create empty Amount
 */
@AutoRegister
object Amount : BaseSelector("amount") {
    override fun SelectorContext.select(caster: Target) {
        val amount = get("amount", 1)
        with(result) {
            while (size > amount) {
                remove(first())
            }
        }
    }

    override fun SelectorContext.filter(caster: Target) {
        select(caster)
    }

    override fun addParameter(dataMap: DataMap, vararg args: Any?) {
        dataMap["amount"] = args.getOrNull(0).castSafely<Int>() ?: 1
    }
}