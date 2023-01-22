package com.skillw.pouvoir.internal.feature.selector

import com.skillw.asahi.util.castSafely
import com.skillw.pouvoir.api.feature.selector.SimpleSelector
import com.skillw.pouvoir.api.feature.selector.Target
import com.skillw.pouvoir.api.feature.selector.toTarget
import com.skillw.pouvoir.api.feature.selector.target.EntityTarget
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import com.skillw.pouvoir.api.plugin.map.DataMap
import com.skillw.pouvoir.util.getRayHits

/**
 * Line
 *
 * @constructor Create empty Line
 */
@AutoRegister
object Line : SimpleSelector("line") {
    override fun SelectorContext.getTargets(caster: Target): List<EntityTarget> {
        val distance = get("distance", 10.0)
        return caster.location.getRayHits(distance).map { it.toTarget() }
    }

    override fun addParameter(dataMap: DataMap, vararg args: Any?) {
        dataMap["distance"] = args.getOrNull(0).castSafely<Double>() ?: 10.0
    }
}