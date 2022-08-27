package com.skillw.pouvoir.internal.core.function.util

data class MapTemplate(val keys: List<String>){
    fun build(values: List<Any>): MutableMap<String, Any> {
        return keys.zip(values).toMap().toMutableMap()
    }
}
