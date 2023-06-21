package com.skillw.asahi.util.condition

import com.skillw.pouvoir.util.condition.ConditionOperator
import com.skillw.pouvoir.util.condition.ConditionOperator.Companion.isConditionOperator
import com.skillw.pouvoir.util.condition.ConditionOperator.Companion.toConditionOperator
import java.util.*

private fun Stack<ConditionOperator>.nextNotLessThan(conditionOperator: ConditionOperator): Boolean {
    return isNotEmpty() && peek() != ConditionOperator.LEFT_BRACKET && peek().priority >= conditionOperator.priority
}

private fun List<Any>.toConditionSuffix(): Queue<Any> {
    val suffix = ArrayDeque<Any>()
    val operators = Stack<ConditionOperator>()
    forEach {
        if (it is Boolean) {
            suffix.offerLast(it)
            return@forEach
        }
        with(operators) {
            when (val operator = it as ConditionOperator) {
                ConditionOperator.LEFT_BRACKET -> push(operator)
                ConditionOperator.RIGHT_BRACKET -> {
                    while (isNotEmpty() && peek() != ConditionOperator.LEFT_BRACKET) {
                        suffix.offerLast(pop())
                    }
                    if (isNotEmpty())
                        pop()
                }

                else -> {
                    while (nextNotLessThan(operator)) {
                        suffix.offerLast(pop())
                    }
                    push(operator)
                }
            }
        }
    }
    while (!operators.isEmpty()) {
        suffix.offerLast(operators.pop())
    }
    return suffix
}

private fun Queue<Any>.calcCondition(): Boolean {
    val calcStack: Stack<Boolean> = Stack()
    while (isNotEmpty()) {
        val obj = poll()
        if (obj is Boolean) {
            calcStack.push(obj)
            continue
        }
        val operator = obj as ConditionOperator
        val a = calcStack.pop()
        val b = calcStack.pop()
        calcStack.push(operator.calc(a, b))
    }
    return calcStack.pop()
}

internal fun List<Any?>.calcCondition(): Boolean {
    val infix = filterNotNull().map {
        val str = toString()
        if (str.isConditionOperator()) str.toConditionOperator()
        else it
    }
    return infix.toConditionSuffix().calcCondition()
}