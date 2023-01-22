package com.skillw.pouvoir.internal.feature.selector

import com.skillw.asahi.util.castSafely
import com.skillw.pouvoir.api.feature.selector.BaseSelector
import com.skillw.pouvoir.api.feature.selector.Target
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import com.skillw.pouvoir.api.plugin.map.DataMap
import com.skillw.pouvoir.internal.feature.selector.Side.Type.*
import org.bukkit.Location

/**
 * Side
 *
 * @constructor Create empty Side
 */
@AutoRegister
object Side : BaseSelector("side") {

    enum class Type {
        IGNORE, SAME, DIFFERENT
    }

    private fun ignore(start: Location, target: Target): Boolean {
        return true
    }

    private fun same(start: Location, target: Target): Boolean {
        return !isObstructed(start, target.location)
    }

    private fun different(start: Location, target: Target): Boolean {
        return isObstructed(start, target.location)
    }

    override fun SelectorContext.filter(caster: Target) {
        val type = get("type", SAME)
        val start = caster.location
        val condition = when (type) {
            IGNORE -> ::ignore
            SAME -> ::same
            DIFFERENT -> ::different
        }
        with(result) {
            removeIf { condition(start, it) }
        }
    }

    override fun addParameter(dataMap: DataMap, vararg args: Any?) {
        dataMap["type"] = args.getOrNull(0).castSafely<Type>() ?: SAME
    }
}