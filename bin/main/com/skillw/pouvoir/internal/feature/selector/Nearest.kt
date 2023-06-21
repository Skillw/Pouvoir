package com.skillw.pouvoir.internal.feature.selector

import com.skillw.asahi.util.castSafely
import com.skillw.pouvoir.api.feature.selector.BaseSelector
import com.skillw.pouvoir.api.feature.selector.Target
import com.skillw.pouvoir.api.feature.selector.toTarget
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import com.skillw.pouvoir.api.plugin.map.DataMap
import com.skillw.pouvoir.util.getNearByEntities
import org.bukkit.Location

/**
 * Nearest
 *
 * @constructor Create empty Nearest
 */
@AutoRegister
object Nearest : BaseSelector("nearest") {
    override fun SelectorContext.select(caster: Target) {
        val amount = get("amount", 1)
        val distance = get("distance", 10.0)
        val start = caster.location
        val comparator = DistanceComparator(start)
        with(result) {
            start.getNearByEntities(distance, distance, distance)
                .map { it.toTarget() }
                .sortedWith(comparator)
                .subList(0, amount)
                .also { addAll(it) }
        }
    }

    override fun SelectorContext.filter(caster: Target) {
        val amount = get("amount", 1)
        val start = caster.location
        val comparator = DistanceComparator(start)
        with(result) {
            val sorted = sortedWith(comparator)
            clear()
            sorted.subList(0, amount).also { addAll(it) }
        }
    }

    override fun addParameter(dataMap: DataMap, vararg args: Any?) {
        dataMap["amount"] = args.getOrNull(0).castSafely<Int>() ?: 10.0
        dataMap["distance"] = args.getOrNull(1).castSafely<Double>() ?: 10.0
    }

    private class DistanceComparator(private val start: Location) : Comparator<Target> {
        override fun compare(targetA: Target, targetB: Target): Int {
            return targetA.location.distanceSquared(start).compareTo(targetB.location.distanceSquared(start))
        }
    }
}