package com.skillw.pouvoir.api.script.top

import com.skillw.pouvoir.internal.core.script.common.top.TopLevel

object TopAPI {

    @JvmStatic
    fun addFunction(
        key: String,
        function: Any,
        source: String = "Unknown",
        info: String = "",
        description: String = "",
    ) {
        TopLevel.addFunction(key, function, source, info, description)
    }

    @JvmStatic
    fun addClass(
        key: String, clazz: Any,
        source: String = "Unknown", info: String = "", description: String = "",
    ) {
        TopLevel.addClass(key, clazz, source, info, description)
    }

    @JvmStatic
    fun addObject(
        key: String, obj: Any,
        source: String = "Unknown", info: String = "", description: String = "",
    ) {
        TopLevel.addObject(key, obj, source, info, description)
    }
}