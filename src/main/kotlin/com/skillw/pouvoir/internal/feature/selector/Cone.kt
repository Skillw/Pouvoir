package com.skillw.pouvoir.internal.feature.selector

import com.skillw.asahi.util.castSafely
import com.skillw.pouvoir.api.feature.selector.SimpleSelector
import com.skillw.pouvoir.api.feature.selector.Target
import com.skillw.pouvoir.api.feature.selector.toTarget
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import com.skillw.pouvoir.api.plugin.map.DataMap
import com.skillw.pouvoir.util.getNearByEntities

/**
 * Cone
 *
 * @constructor Create empty Cone
 */
@AutoRegister
object Cone : SimpleSelector("cone") {

    override fun SelectorContext.getTargets(caster: Target): Collection<Target> {
        val radius = get("radius", 10.0)
        val angle = get("angle", 45.0)
        val start = caster.location
        return start.getNearByEntities(radius, radius, radius)
            .filter { isPointInEntitySector(start, it.location, radius, angle) }
            .map { it.toTarget() }
    }

    override fun addParameter(dataMap: DataMap, vararg args: Any?) {
        dataMap["radius"] = args.getOrNull(0).castSafely<Double>() ?: 10.0
        dataMap["angle"] = args.getOrNull(1).castSafely<Double>() ?: 10.0
    }
}