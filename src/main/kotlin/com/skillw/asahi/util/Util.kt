package com.skillw.asahi.util

import com.skillw.asahi.internal.util.AsahiClassBean
import taboolib.common5.Coerce

@Suppress("IMPLICIT_CAST_TO_ANY")
inline fun <reified T> Any?.castSafely(): T? {
    this ?: return null
    if (T::class.java.isEnum) {
        return Coerce.toEnum(this, T::class.java as Class<out Enum<*>>) as T
    }
    if (this is String && T::class.java == Any::class.java) {
        return if (this.contains(".")) {
            (toDoubleOrNull() ?: this) as T
        } else {
            (toIntOrNull() ?: this) as T
        }
    }
    return when (T::class.java) {
        String::class.java -> toString()
        Int::class.java, Integer::class.java -> Coerce.toInteger(this)
        Long::class.java, java.lang.Long::class.java -> Coerce.toLong(this)
        Short::class.java, java.lang.Short::class.java -> Coerce.toShort(this)
        Double::class.java, java.lang.Double::class.java -> Coerce.toDouble(this)
        Float::class.java, java.lang.Float::class.java -> Coerce.toFloat(this)
        Boolean::class.java, java.lang.Boolean::class.java -> Coerce.toBoolean(this)
        Byte::class.java, java.lang.Byte::class.java -> Coerce.toByte(this)
        else -> this as? T
    } as? T
}

@Suppress("IMPLICIT_CAST_TO_ANY")
inline fun <reified T> Any?.cast(): T {
    return castSafely() as? T? ?: error("Object $this cannot cast to ${T::class.java.name}!")
}

private fun Any?.toDouble() = Coerce.toDouble(this)
private fun Any?.isNumber() = Coerce.asDouble(this).isPresent


fun check(a: Any?, symbol: String, b: Any?): Boolean {
    return when (symbol) {
        "<" -> a.toDouble() < b.toDouble()
        "<=" -> a.toDouble() <= b.toDouble()
        "==" -> when {
            a.isNumber() && b.isNumber() -> a.toDouble() == b.toDouble()
            else -> a == b
        }

        "!=" -> when {
            a.isNumber() && b.isNumber() -> a.toDouble() != b.toDouble()
            else -> a != b
        }

        "===" -> a === b
        "!==" -> a !== b
        ">" -> a.toDouble() > b.toDouble()
        ">=" -> a.toDouble() >= b.toDouble()
        "equals" -> a == b
        "!equals" -> a != b
        "in" -> when (b) {
            is Array<*> -> a in b
            is Collection<*> -> a in b
            //                  Asahirange的返回值就是ClosedRange<Double> 所以直接强转了
            is ClosedRange<*> -> (b as ClosedRange<Double>).contains(a.toDouble())
            else -> a.toString() in b.toString()
        }

        "!in" -> when (b) {
            is Array<*> -> a !in b
            is Collection<*> -> a !in b
            //                  Asahirange的返回值就是ClosedRange<Double> 所以直接强转了
            is ClosedRange<*> -> !(b as ClosedRange<Double>).contains(a.toDouble())
            else -> a.toString() !in b.toString()
        }

        "is" -> when (b) {
            is Class<*> -> b.isInstance(a)
            is String -> a!!::class.java.simpleName.equals(b, true) || a::class.java.name.equals(b, true)
            else -> false
        }

        "!is" -> when (b) {
            is Class<*> -> !b.isInstance(a)
            is String -> !a!!::class.java.simpleName.equals(b, true) && !a::class.java.name.equals(b, true)
            else -> false
        }

        "contains" -> a.toString().contains(b.toString())
        "!contains" -> !a.toString().contains(b.toString())
        "equalsIgnore" -> a.toString().equals(b.toString(), true)
        "!equalsIgnore" -> !a.toString().equals(b.toString(), true)
        else -> {
            println("Unknown symbol $symbol!")
            false
        }
    }
}

fun <R> Any.fastGet(name: String): R? {
    return AsahiClassBean.of(this::class.java)[this, name] as? R?
}

fun Any.fastSet(name: String, value: Any?) {
    AsahiClassBean.of(this::class.java)[this, name] = value
}

fun <R> Any.fastInvoke(method: String, vararg params: Any?): R {
    return AsahiClassBean.of(this::class.java).invoke(this, method, *params) as R
}

fun <R> Any.fastInvoke(method: String, types: Array<Class<*>>, vararg params: Any?): R {
    return AsahiClassBean.of(this::class.java).invoke(this, method, types, *params) as R
}

