package com.skillw.pouvoir.util

import com.skillw.asahi.api.AsahiAPI.analysis
import com.skillw.asahi.api.member.context.AsahiContext
import taboolib.common.util.unsafeLazy
import taboolib.common5.Coerce
import kotlin.reflect.KProperty

/**
 * @className DataMap
 *
 * 专用于存储数据的Map
 *
 * @author Glom
 * @date 2022/8/14 7:45 Copyright 2022 user. All rights reserved.
 */
open class AsahiDataMap : HashMap<String, Any>() {

    operator fun getValue(thisRef: Any?, property: KProperty<*>): Any? {
        return (thisRef as AsahiDataMap)[property.name]
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Any) {
        (thisRef as AsahiDataMap)[property.name] = value
    }

    /**
     * 获取任意对象
     *
     * @param key String 键
     * @param default T? 默认值
     * @return T 类型值11
     * @receiver DataMap
     */
    inline fun <reified T : Any> get(key: String, default: T? = null): T {
        return getAs(key) ?: default ?: error(
            "get ${T::class.simpleName} error"
        )
    }

    inline fun <reified T> getAs(key: String): T? {
        return when (T::class) {
            String::class -> getStringInner(key) as? T?
            Int::class -> getIntInner(key) as? T?
            Long::class -> getLongInner(key) as? T?
            Float::class -> getFloatInner(key) as? T?
            Double::class -> getDoubleInner(key) as? T?
            Boolean::class -> getBooleanInner(key) as? T?
            Byte::class -> getByteInner(key) as? T?
            Short::class -> getShortInner(key) as? T?
            List::class -> getListInner(key) as? T?
            else -> get(key) as? T?
        }
    }

    /**
     * 获取任意对象
     *
     * @param key String 键
     * @param default T? 默认值
     * @return T 类型值11
     * @receiver DataMap
     */
    inline fun <reified T> getOrDefault(key: String, defaultFunc: () -> T? = { null }): T {
        return getAs(key) ?: defaultFunc() ?: error(
            "get ${T::class.simpleName} error"
        )
    }

    val context by unsafeLazy {
        AsahiContext.create(this)
    }

    private fun Any.analysisCast(): Any {
        return if (this is String) analysis(context) else this
    }

    /**
     * 根据键获取any
     *
     * @param key
     * @param processData
     * @return
     */
    fun getAny(key: String): Any {
        return get<Any>(key)
    }

    /**
     * 根据键获取list
     *
     * @param key
     * @param processData
     * @return
     */
    fun getList(key: String): List<Any> {
        return get<List<Any>>(key)
    }

    /**
     * 根据键获取short
     *
     * @param key
     * @param processData
     * @return
     */
    fun getShort(key: String): Short {
        return get<Short>(key)
    }

    /**
     * 根据键获取byte
     *
     * @param key
     * @param processData
     * @return
     */
    fun getByte(key: String): Byte {
        return get<Byte>(key)
    }

    /**
     * 根据键获取boolean
     *
     * @param key
     * @param processData
     * @return
     */
    fun getBoolean(key: String): Boolean {
        return get<Boolean>(key)
    }

    /**
     * 根据键获取double
     *
     * @param key
     * @param processData
     * @return
     */
    fun getDouble(key: String): Double {
        return get<Double>(key)
    }

    /**
     * 根据键获取float
     *
     * @param key
     * @param processData
     * @return
     */
    fun getFloat(key: String): Float {
        return get<Float>(key)
    }

    /**
     * 根据键获取long
     *
     * @param key
     * @param processData
     * @return
     */
    fun getLong(key: String): Long {
        return get<Long>(key)
    }

    /**
     * 根据键获取int
     *
     * @param key
     * @param processData
     * @return
     */
    fun getInt(key: String): Int {
        return get<Int>(key)
    }

    /**
     * 根据键获取string
     *
     * @param key
     * @param processData
     * @return
     */
    fun getString(key: String): String {
        return get<String>(key)
    }

    companion object {

        /**
         * 根据键获取any
         *
         * @param key
         * @param processData
         * @return
         */
        fun AsahiDataMap.getAnyInner(key: String): Any? {
            return get(key)
        }

        /**
         * 根据键获取list
         *
         * @param key
         * @param processData
         * @return
         */
        fun AsahiDataMap.getListInner(key: String): List<Any>? {
            return (get(key) as? List<Any>?)?.map { it.analysisCast() }
        }

        /**
         * 根据键获取short
         *
         * @param key
         * @param processData
         * @return
         */
        fun AsahiDataMap.getShortInner(key: String): Short? {
            return Coerce.toShort((get(key) ?: return null).analysisCast())
        }

        /**
         * 根据键获取byte
         *
         * @param key
         * @param processData
         * @return
         */
        fun AsahiDataMap.getByteInner(key: String): Byte? {
            return Coerce.toByte((get(key) ?: return null).analysisCast())
        }

        /**
         * 根据键获取boolean
         *
         * @param key
         * @param processData
         * @return
         */
        fun AsahiDataMap.getBooleanInner(key: String): Boolean? {
            return Coerce.toBoolean((get(key) ?: return null).analysisCast())
        }

        /**
         * 根据键获取double
         *
         * @param key
         * @param processData
         * @return
         */
        fun AsahiDataMap.getDoubleInner(key: String): Double? {
            return Coerce.toDouble((get(key) ?: return null).analysisCast())
        }

        /**
         * 根据键获取float
         *
         * @param key
         * @param processData
         * @return
         */
        fun AsahiDataMap.getFloatInner(key: String): Float? {
            return Coerce.toFloat((get(key) ?: return null).analysisCast())
        }

        /**
         * 根据键获取long
         *
         * @param key
         * @param processData
         * @return
         */
        fun AsahiDataMap.getLongInner(key: String): Long? {
            return Coerce.toLong((get(key) ?: return null).analysisCast())
        }

        /**
         * 根据键获取int
         *
         * @param key
         * @param processData
         * @return
         */
        fun AsahiDataMap.getIntInner(key: String): Int? {
            return get(key)?.let { Coerce.toInteger(it.analysisCast()) }
        }

        /**
         * 根据键获取string
         *
         * @param key
         * @param processData
         * @return
         */
        fun AsahiDataMap.getStringInner(key: String): String? {
            return get(key)?.run { analysisCast().toString() }
        }
    }
}