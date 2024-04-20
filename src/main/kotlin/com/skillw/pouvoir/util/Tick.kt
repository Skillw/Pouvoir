package com.skillw.pouvoir.util

import com.skillw.pouvoir.internal.feature.database.PouvoirContainer
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.submit
import taboolib.common5.Coerce

object Tick {
    var currentTick: Long = 0
        @Synchronized
        get
        private set

    @Awake(LifeCycle.ACTIVE)
    fun start() {
        currentTick = Coerce.toLong(PouvoirContainer["COMMON", "CLOCK_TICKS"])
        submit(async = true, period = 1, delay = 0) {
            currentTick++
        }
    }

    @Awake(LifeCycle.DISABLE)
    fun disable() {
        PouvoirContainer["COMMON", "CLOCK_TICKS"] = currentTick.toString()
    }
}