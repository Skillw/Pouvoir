package com.skillw.pouvoir.util.condition

/**
 * @className A
 *
 * @author Glom
 * @date 2023/1/16 3:33 Copyright 2023 user. All rights reserved.
 */
internal enum class ConditionOperator(
    private val symbol: String,
    val priority: Int,
    val calc: (Boolean, Boolean) -> Boolean = { _, _ -> true },
) {
    AND("and", 1, { a, b -> a && b }),
    OR("or", 1, { a, b -> b || a }),
    LEFT_BRACKET("(", 2),
    RIGHT_BRACKET(")", 2);

    override fun toString(): String {
        return symbol
    }

    companion object {
        private val symbols = ConditionOperator.values().map { it.symbol }.toHashSet()
        fun String.isConditionOperator(): Boolean = this in symbols
        fun String.toConditionOperator(): ConditionOperator {
            return when (this) {
                "and", "&&" -> AND
                "or", "||" -> OR
                "(" -> LEFT_BRACKET
                ")" -> RIGHT_BRACKET
                else -> error("No such Operator $this")
            }
        }
    }
}