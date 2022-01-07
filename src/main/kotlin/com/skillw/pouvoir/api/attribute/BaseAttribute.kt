package com.skillw.pouvoir.api.attribute

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.`object`.BaseObject

interface BaseAttribute : BaseObject {
    val names: Collection<String>
    override fun register() {
        Pouvoir.baseAttributeManager.register(this)
    }

}