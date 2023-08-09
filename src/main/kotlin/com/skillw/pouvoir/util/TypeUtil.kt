package com.skillw.pouvoir.util


import org.bukkit.entity.LivingEntity
import taboolib.common5.Coerce
import taboolib.module.nms.ItemTag
import taboolib.module.nms.ItemTagData
import taboolib.module.nms.ItemTagList
import taboolib.module.nms.ItemTagType


fun String.cast(): Any {
    return when {
        startsWith("(int) ") -> Coerce.toInteger(substring(6))
        startsWith("(long) ") -> Coerce.toLong(substring(7))
        startsWith("(float) ") -> Coerce.toFloat(substring(8))
        startsWith("(double) ") -> Coerce.toDouble(substring(9))
        startsWith("(boolean) ") -> Coerce.toBoolean(substring(10))
        startsWith("(char) ") -> Coerce.toChar(substring(7))
        startsWith("(byte) ") -> Coerce.toByte(substring(7))
        startsWith("(short) ") -> Coerce.toShort(substring(8))
        else -> this
    }
}


fun Any.toTypeString(): Any {
    return when (this) {
        is Int -> "(int) $this"
        is Long -> "(long) $this"
        is Float -> "(float) $this"
        is Double -> "(double) $this"
        is Boolean -> "(boolean) $this"
        is Char -> "(char) $this"
        is Byte -> "(byte) $this"
        is Short -> "(short) $this"
        is Map<*, *> -> valuesToTypeString()
        is List<*> -> valuesToTypeString()
        is ByteArray -> map { it.toTypeString() }
        is IntArray -> map { it.toTypeString() }
        else -> this.toString()
    }
}


fun List<*>.valuesToTypeString(): List<Any> {
    return map { it?.toTypeString() ?: "null" }
}


fun Map<*, *>.valuesToTypeString(): Map<String, Any> {
    return mapKeys {
        it.key.toString()
    }.mapValues { (_, value) ->
        value?.toTypeString() ?: "null"
    }
}


fun MutableMap<String, Any?>.filterNonNull(): MutableMap<String, Any> {
    return entries.filter { it.value != null }.associate { Pair(it.key, it.value!!) }.toMutableMap()
}


fun MutableMap<String, Any>.removeDeep(path: String) {
    val splits = path.split(".")
    if (splits.isEmpty()) {
        this.remove(path)
        return
    }
    var compound = this
    var temp: MutableMap<String, Any>
    for (node in splits) {
        if (node.equals(splits.last(), ignoreCase = true)) {
            compound.remove(node)
        }
        compound[node].also { temp = ((it as MutableMap<String, Any>?) ?: return) }
        compound = temp
    }
}

internal fun ItemTag.toMutableMap(strList: List<String> = emptyList()): MutableMap<String, Any> {
    val map = HashMap<String, Any>()
    for (it in this) {
        val key = it.key
        if (strList.contains(key)) continue
        val value = it.value.obj<Any>()
        map[key] = value
    }
    return map
}

internal fun ItemTagList.toObjList(): List<Any> {
    return map { it.obj() }
}

internal fun <T> ItemTagData.obj(): T {
    val value = when (this.type) {
        ItemTagType.BYTE -> this.asByte()
        ItemTagType.SHORT -> this.asShort()
        ItemTagType.INT -> this.asInt()
        ItemTagType.LONG -> this.asLong()
        ItemTagType.FLOAT -> this.asFloat()
        ItemTagType.DOUBLE -> this.asDouble()
        ItemTagType.STRING -> this.asString()
        ItemTagType.BYTE_ARRAY -> this.asByteArray()
        ItemTagType.INT_ARRAY -> this.asIntArray()
        ItemTagType.COMPOUND -> this.asCompound()
        ItemTagType.LIST -> this.asList()
        else -> this.asString()
    } as T
    return when (value) {
        is ItemTag -> {
            value.toMutableMap()
        }

        is ItemTagList -> {
            val list = ArrayList<Any>()
            value.forEach {
                list.add(it.obj())
            }
            list
        }

        else -> value
    } as T
}

fun <T : Any> T.replaceThenCalc(replacement: Map<String, String>, entity: LivingEntity?): Any {
    return when (this) {
        is Map<*, *> -> {
            val map = HashMap<String, Any>()
            forEach { (key, value) ->
                key ?: return@forEach
                value ?: return@forEach
                map[key.toString()] = value.replaceThenCalc(replacement, entity)
            }
            map
        }

        is List<*> -> {
            val list = ArrayList<Any>()
            mapNotNull { it }.forEach {
                list.add(it.replaceThenCalc(replacement, entity))
            }
            list
        }

        is String -> replacement(replacement).calculateInline(entity)

        else -> this
    }
}


fun <T : Any> T.deepClone(): Any {
    return when (this) {
        is Map<*, *> -> {
            val map = HashMap<String, Any>()
            forEach { (key, value) ->
                key ?: return@forEach
                value ?: return@forEach
                map[key.toString()] = value.deepClone()
            }
            map
        }

        is List<*> -> {
            val list = ArrayList<Any>()
            mapNotNull { it }.forEach {
                list.add(it.deepClone())
            }
            list
        }

        else -> this
    }
}
