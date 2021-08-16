package com.skillw.pouvoir.api.formula

import com.skillw.pouvoir.Pouvoir.rpgPlaceHolderAPI
import com.skillw.pouvoir.util.CalculationUtils
import org.bukkit.entity.LivingEntity
import java.util.concurrent.ConcurrentHashMap

/**
 * ClassName : com.skillw.classsystem.formula.Formula
 * Created by Glom_ on 2021-03-25 20:49:58
 * Copyright  2021 user. All rights reserved.
 */
class Formula(private val formula: String, private val livingEntity: LivingEntity) {
    private val replacements = ConcurrentHashMap<String, String>()
    fun replace(replaced: String, replace: String): Formula {
        replacements[replaced] = replace
        return this
    }

    fun formula(): String {
        return rpgPlaceHolderAPI.replace(livingEntity, CalculationUtils.replace(formula, replacements))
    }

    fun result(): Double {
        return CalculationUtils.getResult(formula(), livingEntity, replacements)
    }
}