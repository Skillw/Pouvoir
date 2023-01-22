package com.skillw.asahi.api.member.namespace

import com.skillw.asahi.api.AsahiManager
import com.skillw.asahi.api.member.AsahiRegistrable
import com.skillw.asahi.api.member.parser.infix.namespacing.BaseInfix
import com.skillw.asahi.api.member.parser.prefix.namespacing.BasePrefix

/**
 * Namespace
 *
 * @constructor Create empty Namespace
 * @property key
 * @property shared
 */
open class Namespace(override val key: String, val shared: Boolean = false) : AsahiRegistrable<String> {
    /** Prefix 前缀解释器容器， Token -> 前缀解释器 */
    internal val prefixMap = HashMap<String, BasePrefix<*>>()

    /** Infix 中缀解释器容器， 类型 -> 中缀解释器 */
    internal val infixMap = HashMap<Class<*>, BaseInfix<*>>()

    /** 所有的中缀 Tokens */
    internal val allInfixTokens = HashSet<String>()

    /**
     * 有无前缀解释器
     *
     * @param token token
     * @return
     */
    fun hasPrefix(token: String): Boolean {
        return prefixMap.containsKey(token)
    }

    /**
     * 获取前缀解释器
     *
     * @param token token
     * @return
     */
    fun getPrefix(token: String): BasePrefix<*>? {
        return prefixMap[token]
    }

    /**
     * 注册前缀解释器
     *
     * @param prefix 前缀解释器
     */
    fun registerPrefix(prefix: BasePrefix<*>) {
        val keys = listOf(prefix.key, *prefix.alias)
        keys.forEach { key ->
            prefixMap[key] = prefix
        }
    }

    /**
     * 获取中缀解释器
     *
     * @param type 对象类
     * @param T 对象类型
     * @return
     */
    fun <T : Any> getInfix(type: Class<T>): BaseInfix<T> {
        return infixMap[type] as? BaseInfix<T>? ?: kotlin.run {
            val infix = BaseInfix.createInfix(type)
            infixMap.entries.sortedWith { a, b ->
                if (a.key.isAssignableFrom(b.key)) -1 else 1
            }.forEach {
                infix.putAll(it.value)
            }
            infix.apply { register() }
        }
    }

    /**
     * 注册中缀解释器
     *
     * @param infix 中缀解释器
     */
    fun registerInfix(infix: BaseInfix<*>) {
        val type = infix.key
        if (infixMap.containsKey(type)) {
            infixMap[type]?.putAll(infix)
            return
        }
        infixMap[type] = infix
        infix.infixMap.keys.forEach(allInfixTokens::add)
    }

    /**
     * 有无此中缀Token
     *
     * @param token 中缀Token
     * @return 有无此中缀token
     */
    fun hasInfix(token: String?): Boolean {
        return allInfixTokens.contains(token)
    }

    /**
     * 有无此类型的中缀解释器
     *
     * @param type 对象类型
     * @return 有无此类型的中缀解释器
     */
    fun hasInfix(type: Class<*>): Boolean {
        return infixMap.containsKey(type) || infixMap.keys.any { it.isAssignableFrom(type) }
    }

    /**
     * 获取一个对象的类型的中缀解释器
     *
     * @param any 对象
     * @param T 对象类型
     * @return 对象类型的中缀解释器
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Any> infixOf(any: T): BaseInfix<T> {
        return getInfix(any::class.java as Class<T>)
    }

    override fun register() {
        AsahiManager.namespaces[key] = this
    }

}
