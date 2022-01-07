package com.skillw.pouvoir.api.plugin

import com.skillw.pouvoir.api.able.Keyable
import com.skillw.pouvoir.api.able.Pluginable
import com.skillw.pouvoir.api.manager.ManagerData
import com.skillw.pouvoir.api.manager.TotalManager
import java.util.concurrent.ScheduledThreadPoolExecutor

interface SubPouvoir : Pluginable, Keyable<String> {
    var managerData: ManagerData
    val poolExecutor: ScheduledThreadPoolExecutor

    override fun register() {
        TotalManager.register(this.plugin)
    }

    fun reloadAll() {
        TotalManager.reload(this)
    }
}