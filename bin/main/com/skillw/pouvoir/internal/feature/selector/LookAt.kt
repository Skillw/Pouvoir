package com.skillw.pouvoir.internal.feature.selector

import com.skillw.asahi.util.castSafely
import com.skillw.pouvoir.api.feature.selector.SimpleSelector
import com.skillw.pouvoir.api.feature.selector.Target
import com.skillw.pouvoir.api.feature.selector.toTarget
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import com.skillw.pouvoir.api.plugin.map.DataMap
import com.skillw.pouvoir.util.getRayHit

/**
 * LookAt
 *
 * @constructor Create empty LookAt
 */
@AutoRegister
object LookAt : SimpleSelector("lookAt") {
    override fun SelectorContext.getTargets(caster: Target): Collection<Target> {
        val distance = get("distance", 10.0)
        return caster.location.getRayHit(distance)?.toTarget()?.let { hashSetOf(it) } ?: emptySet()
    }


    override fun addParameter(dataMap: DataMap, vararg args: Any?) {
        dataMap["distance"] = args.getOrNull(0).castSafely<Double>() ?: 10.0
    }
}