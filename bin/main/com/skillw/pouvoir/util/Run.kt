package com.skillw.pouvoir.util

import java.util.*
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource

/**
 * @className Runnable
 *
 * @author Glom
 * @date 2023/1/7 22:18 Copyright 2024 Glom.
 */


fun <T> safe(run: () -> T): T? {
    return runCatching { run() }.run {
        if (isSuccess) getOrNull()
        else {
            exceptionOrNull()?.printStackTrace()
            null
        }
    }
}

fun <T> silent(run: () -> T): T? {
    return runCatching { run() }.getOrNull()
}

@OptIn(ExperimentalTime::class)
fun time(times: Int = 1, exec: () -> Unit): Duration {
    val mark = TimeSource.Monotonic.markNow()
    repeat(times) {
        exec()
    }
    return mark.elapsedNow()
}

@OptIn(ExperimentalTime::class)
fun <R> time(exec: () -> R): Pair<R, Duration> {
    val mark = TimeSource.Monotonic.markNow()
    return exec() to mark.elapsedNow()
}

@OptIn(ExperimentalTime::class)
fun <R> timeSafe(exec: () -> R): Pair<R?, Duration> {
    val mark = TimeSource.Monotonic.markNow()
    return safe { exec() } to mark.elapsedNow()
}

@OptIn(ExperimentalTime::class)
fun <R> timeSafeSilent(exec: () -> R): Pair<R?, Duration> {
    val mark = TimeSource.Monotonic.markNow()
    return silent { exec() } to mark.elapsedNow()
}