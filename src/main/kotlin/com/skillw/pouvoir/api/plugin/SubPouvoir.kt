package com.skillw.pouvoir.api.plugin

import com.skillw.pouvoir.api.able.Keyable
import com.skillw.pouvoir.api.able.Pluginable
import com.skillw.pouvoir.api.manager.ManagerData
import com.skillw.pouvoir.api.manager.TotalManager

interface SubPouvoir : Pluginable, Keyable<String> {
    var managerData: ManagerData
    override fun register() {
        TotalManager.register(this.plugin)
    }

    fun reloadAll() {
        TotalManager.reload(this)
    }
}