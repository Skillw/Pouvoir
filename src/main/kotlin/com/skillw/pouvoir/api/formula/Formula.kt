package com.skillw.pouvoir.api.formula

import com.skillw.pouvoir.Pouvoir.pouPlaceHolderAPI
import com.skillw.pouvoir.util.CalculationUtils
import com.skillw.pouvoir.util.Pair
import com.skillw.pouvoir.util.StringUtils.replacement
import org.bukkit.entity.LivingEntity
import java.math.BigDecimal
import java.util.concurrent.ConcurrentHashMap

/**
 * ClassName : com.skillw.pouvoir.formula.Formula
 * Created by Glom_ on 2021-03-25 20:49:58
 * Copyright  2021 user. All rights reserved.
 */
class Formula(private val formula: String, private val livingEntity: LivingEntity) {
    private val replacements = ConcurrentHashMap<String, String>()
    fun replace(vararg pairs: Pair<String, String>): Formula {
        pairs.forEach {
            replacements[it.key] = it.value
        }
        return this
    }

    fun formula(): String {
        return pouPlaceHolderAPI.replace(livingEntity, formula.replacement(replacements.toMap()))
    }

    fun result(): BigDecimal {
        return CalculationUtils.getResult(formula(), livingEntity, replacements)
    }
}