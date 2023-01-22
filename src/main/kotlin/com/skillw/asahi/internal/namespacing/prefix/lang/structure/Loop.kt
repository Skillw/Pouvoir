package com.skillw.asahi.internal.namespacing.prefix.lang.structure

import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.member.context.LoopContext
import com.skillw.asahi.api.member.parser.prefix.namespacing.PrefixParser
import com.skillw.asahi.api.member.quest.Quester
import com.skillw.asahi.api.prefixParser
import com.skillw.asahi.api.quest
import com.skillw.asahi.api.quester
import com.skillw.asahi.internal.context.AsahiLoopContext
import com.skillw.asahi.internal.context.AsahiLoopContext.Companion.loopContext
import java.util.*

/**
 * @className Loop
 *
 * @author Glom
 * @date 2023/1/14 0:25 Copyright 2023 user. All rights reserved.
 */
@AsahiPrefix(["while"], "lang")
private fun `while`() = prefixParser {
    val condition = questCondition("label", "then")
    runLoop { loopOnce ->
        while (condition.get()) {
            when (loopOnce()) {
                LoopContext.Result.BREAK -> break
                LoopContext.Result.CONTINUE -> continue
            }
        }
    }
}

@AsahiPrefix(["repeat"], "lang")
private fun repeat() = prefixParser {
    val time = quest<Int>()
    val indexName = if (expect("with")) quest() else quester { "index" }
    runLoop { loopOnce ->
        val indexParam = indexName.get()
        for (i in 0 until time.get()) {
            context()[indexParam] = i
            when (loopOnce()) {
                LoopContext.Result.BREAK -> break
                LoopContext.Result.CONTINUE -> continue
            }
        }
    }
}

@AsahiPrefix(["foreach", "for"], "lang")
private fun foreach() = prefixParser {
    val paramName = next()
    expect("in")
    val getter = quest<Any>()
    runLoop { loopOnce ->
        when (val obj = getter.get()) {
            is Array<*> -> {
                for (item in obj) {
                    context()[paramName] = item ?: continue
                    when (loopOnce()) {
                        LoopContext.Result.BREAK -> break
                        LoopContext.Result.CONTINUE -> continue
                    }
                }
            }

            is Collection<*> -> {
                for (item in obj) {
                    context()[paramName] = item ?: continue
                    when (loopOnce()) {
                        LoopContext.Result.BREAK -> break
                        LoopContext.Result.CONTINUE -> continue
                    }
                }
            }
        }
    }
}


private fun PrefixParser<*>.runLoop(
    loop: AsahiLoopContext.(() -> LoopContext.Result) -> Unit,
): Quester<Unit> {
    val label = if (expect("label")) next() else UUID.randomUUID().toString()
    expect("then")
    val process = parseScript()
    return result {
        val loopContext = context().loopContext(label)

        //循环一次
        fun loopOnce(): LoopContext.Result {
            loopContext.run {
                reset()
                process.isExit {
                    when {
                        isBreak -> true
                        isContinue -> true.also { isContinue = false }
                        isExit() -> true
                        else -> false
                    }
                }.run()
                return when {
                    isBreak -> LoopContext.Result.BREAK
                    isContinue -> LoopContext.Result.CONTINUE.also { isContinue = false }
                    isExit() -> LoopContext.Result.BREAK
                    else -> LoopContext.Result.CONTINUE
                }
            }
        }
        loopContext.run {
            loop { loopOnce() }
        }
        context().putAllIfExists(loopContext)
    }
}

@AsahiPrefix(["break"], "lang")
private fun `break`() = prefixParser {
    val labelGetter = if (expect("the")) quest() else quester { (context() as LoopContext).label }
    result {
        if (this !is LoopContext) return@result
        val label = labelGetter.get()
        searchLabel(label).apply {
            isBreak = true
            subLoops.forEach {
                it.isBreak = true
            }
        }
    }
}

@AsahiPrefix(["continue"], "lang")
private fun `continue`() = prefixParser {
    val labelGetter = if (expect("the")) quest() else quester { (context() as LoopContext).label }
    result {
        if (this !is LoopContext) return@result
        val label = labelGetter.get()
        searchLabel(label).apply {
            isContinue = true
            subLoops.forEach {
                it.isBreak = true
            }
        }

    }
}