package com.skillw.pouvoir.internal.feature.selector

import com.skillw.asahi.util.castSafely
import com.skillw.pouvoir.api.feature.selector.SimpleSelector
import com.skillw.pouvoir.api.feature.selector.Target
import com.skillw.pouvoir.api.feature.selector.toTarget
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import com.skillw.pouvoir.api.plugin.map.DataMap
import com.skillw.pouvoir.util.getNearByEntities

/**
 * Range
 *
 * @constructor Create empty Range
 */
@AutoRegister
object Range : SimpleSelector("range") {
    override fun SelectorContext.getTargets(caster: Target): Collection<Target> {
        val x = get("x", 10.0)
        val y = get("y", x)
        val z = get("z", x)
        return caster.location.getNearByEntities(x, y, z).map { it.toTarget() }
    }


    override fun addParameter(dataMap: DataMap, vararg args: Any?) {
        dataMap["x"] = args.getOrNull(0).castSafely<Double>() ?: 10.0
        dataMap["y"] = args.getOrNull(1).castSafely<Double>() ?: 10.0
        dataMap["z"] = args.getOrNull(2).castSafely<Double>() ?: 10.0
    }
}