package com.skillw.pouvoir.util

import java.util.*

object WeightUtils {
    @JvmStatic
    fun <T> getWeightRandom(list: List<Pair<Number, T>>): T {
        val weightMap: TreeMap<Number, T> = TreeMap<Number, T>()
        for (pair in list) {
            val weight = pair.first.toDouble()
            val obj = pair.second
            val lastWeight = (if (weightMap.size == 0) 0 else weightMap.lastKey()).toDouble()
            weightMap[weight + lastWeight] = obj
        }
        val randomWeight = weightMap.lastKey().toDouble() * Math.random()
        val tailMap = weightMap.tailMap(randomWeight, false)
        return weightMap[tailMap.firstKey()]!!
    }

}