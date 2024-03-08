package com.skillw.asahi.util

import com.skillw.asahi.api.AsahiAPI.compile
import com.skillw.asahi.api.lazyQuester
import com.skillw.asahi.api.member.context.AsahiContext

/**
 * @className Deserialize
 *
 * @author Glom
 * @date 2023/1/15 23:55 Copyright 2024 Glom.
 */

private fun Any?.mapValue(vararg namespaces: String): Any {
    return when (this) {
        is Map<*, *> -> toLazyMap()
        is List<*> -> map { it.mapValue(*namespaces) }
        is String -> compile(*namespaces).let { lazyQuester { it.get() } }
        else -> lazyQuester { this }
    }
}

fun Map<*, *>.toLazyMap(vararg namespaces: String): Map<String, Any> {
    return mapKeys { it.key.toString() }.mapValues { (_, value) ->
        value.mapValue(*namespaces)
    }
}

fun Map<*, *>.toLazyContext(vararg namespaces: String): Map<String, Any> {
    return AsahiContext.create(toLazyMap(*namespaces).toMutableMap())
}
