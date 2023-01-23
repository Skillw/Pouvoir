package com.skillw.pouvoir.internal.core.asahi.prefix.bukkit

import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.prefixParser
import com.skillw.asahi.api.quest
import com.skillw.asahi.api.quester
import com.skillw.pouvoir.internal.core.asahi.util.delay
import taboolib.common.platform.function.submit
import taboolib.common.platform.function.submitAsync
import java.util.*
import java.util.concurrent.CompletableFuture

/**
 * @className Task
 *
 * @author Glom
 * @date 2023/1/14 0:56 Copyright 2023 user. All rights reserved.
 */
@AsahiPrefix(["async", "taskAsync"])
private fun async() = prefixParser {
    var delayGetter = quester { 0L }
    var periodGetter = quester { 0L }
    if (expect("in")) {
        delayGetter = questTick()
    }
    if (expect("every")) {
        periodGetter = questTick()
    }
    val content = parseScript()
    result {
        val delay = delayGetter.get()
        val period = periodGetter.get()

        val asyncContext = context().clone()
        val completable = CompletableFuture<Any?>()
        submitAsync(delay == 0L, delay, period) {
            if (completable.isCancelled) return@submitAsync
            completable.complete(content.run(asyncContext))
            context().putAllIfExists(asyncContext)
        }
        completable.autoCancelled()
        addTask(completable)
    }
}

@AsahiPrefix(["sync", "taskSync", "task"])
private fun sync() = prefixParser {
    var delayGetter = quester { 0L }
    var periodGetter = quester { 0L }
    if (expect("in")) {
        delayGetter = questTick()
    }
    if (expect("every")) {
        periodGetter = questTick()
    }
    val content = parseScript()
    result {
        val delay = delayGetter.get()
        val period = periodGetter.get()

        val submitContext = context().clone()
        val completable = CompletableFuture<Any?>()
        submit(delay == 0L, false, delay, period) {
            if (completable.isCancelled) return@submit
            completable.complete(content.run(submitContext))
            context().putAllIfExists(submitContext)
        }
        completable.autoCancelled()
        addTask(completable)
    }
}

@AsahiPrefix(["inSync"])
private fun inSync() = prefixParser {
    val content = parseScript()
    result {
        taboolib.common.util.sync {
            context().clone().run {
                val result = content.run()
                context().putAllIfExists(this)
                result
            }
        }
    }
}

@AsahiPrefix(["await"])
private fun await() = prefixParser<Any?> {
    val any = expect("any")
    val all = expect("all")
    if (peek() != "[" && all) {
        return@prefixParser result {
            awaitAllTask()
        }
    }
    val list = if (peek() == "[") quest() else quest<CompletableFuture<Any?>>().quester { listOf(it) }
    result {
        val tasks = list.get().toTypedArray()
        if (any)
            CompletableFuture.anyOf(*tasks).join()
        else
            CompletableFuture.allOf(*tasks).join()

    }
}


@AsahiPrefix(["wait", "delay", "sleep"], "lang")
private fun delay() = prefixParser {
    val tick = questTick()
    result {
        delay(tick.get())
    }
}