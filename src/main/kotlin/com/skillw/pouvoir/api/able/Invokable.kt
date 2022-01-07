package com.skillw.pouvoir.api.able

import com.skillw.pouvoir.api.`object`.BaseObject

interface Invokable<T : BaseObject> {
    fun invoke(key: String, obj: T, argsMap: MutableMap<String, Any> = HashMap(), vararg args: Any): Any?
}