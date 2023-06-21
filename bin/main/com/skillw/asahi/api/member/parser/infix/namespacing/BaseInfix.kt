package com.skillw.asahi.api.member.parser.infix.namespacing

import com.skillw.asahi.api.AsahiManager
import com.skillw.asahi.api.member.AsahiRegistrable
import com.skillw.asahi.api.member.context.InfixContext
import com.skillw.asahi.api.member.namespace.Namespacing


/**
 * @className BaseInfix
 *
 * @author Glom
 * @date 2022年12月27日 Copyright 2022 user. All rights reserved.
 */
abstract class BaseInfix<T : Any>(override val key: Class<out T>, override val namespace: String = "common") :
    AsahiRegistrable<Class<out T>>, Namespacing {

    constructor(type: Class<T>, vararg pairs: Pair<String, InfixExecutor<T>>) : this(type) {
        infixMap.putAll(pairs)
    }

    /** 中缀Token 中缀解释器执行内容 */
    internal val infixMap = HashMap<String, InfixExecutor<T>>()

    /**
     * 执行中缀解释器
     *
     * @param obj 对象
     * @return 结果
     *///  动作上下文        执行动作
    fun InfixContext.infix(obj: T): Any? {
        return infixMap[token]?.run { execute(obj) }
    }

    /**
     * 添加中缀解释器执行内容
     *
     * @param executor 执行内容
     * @receiver
     */
    infix fun String.to(executor: InfixContext.(T) -> Any?) {
        infix(this, executor = executor)
    }

    /**
     * 添加中缀解释器执行内容
     *
     * @param executor 执行内容
     * @receiver
     */
    infix fun Collection<String>.to(executor: InfixContext.(T) -> Any?) {
        infix(*toTypedArray(), executor = executor)
    }

    /**
     * 添加中缀解释器执行内容
     *
     * @param executor 执行内容
     * @receiver
     */
    infix fun Array<String>.to(executor: InfixContext.(T) -> Any?) {
        infix(*this, executor = executor)
    }

    /**
     * 添加中缀解释器执行内容
     *
     * @param keys 中缀token
     * @param executor 执行内容
     * @return
     * @receiver
     */
    fun infix(vararg keys: String, executor: InfixContext.(T) -> Any?): BaseInfix<T> {
        keys.forEach { key ->
            infixMap[key] = InfixExecutor { executor(it) }
        }
        return this
    }

    /**
     * 添加中缀解释器执行内容
     *
     * @param pair 中缀token to 执行内容
     */
    infix fun `infix`(pair: Pair<String, InfixContext.(T) -> Any?>) {
        infix(pair.first, executor = pair.second)
    }

    /**
     * 删除中缀解释器执行内容
     *
     * @param token 中缀token
     * @return
     */
    fun removeInfix(token: String): BaseInfix<T> {
        infixMap.remove(token)
        return this
    }

    override fun register() {
        AsahiManager.getNamespace(namespace).registerInfix(this)
    }

    /**
     * 填入其它中缀解释器的执行内容
     *
     * @param other 其它中缀解释器
     */
    fun putAll(other: BaseInfix<*>) {
        if (other.key != key && !other.key.isAssignableFrom(key)) return
        other.infixMap.forEach { (key, action) ->
            infix(key) {
                action.run(this, it)
            }
        }
    }

    override fun toString(): String {
        return "AsahiInfix { $key ${infixMap.keys} }"
    }

    companion object {
        //给Java用的
        @JvmStatic
        fun <T : Any> createInfix(type: Class<T>, namespace: String = "common"): BaseInfix<T> {
            return object : BaseInfix<T>(type, namespace) {}
        }

        /**
         * 创建中缀解释器
         *
         * @param type Class<T> 类型
         * @param namespace String 命名空间
         * @param receiver 处理中缀解释器
         * @return BaseInfix<T> 中缀解释器
         */
        @JvmStatic
        fun <T : Any> createInfix(
            type: Class<T>,
            namespace: String = "common",
            receiver: BaseInfix<T>.() -> Unit,
        ): BaseInfix<T> {
            return object : BaseInfix<T>(type, namespace) {}.apply(receiver)
        }

        /**
         * 中缀解释器执行内容
         *
         * @param type Class<T> 类型
         * @param keys Array<out String> 中缀token
         * @param namespace String 命名空间
         * @param executor 执行内容
         */
        @JvmStatic
        fun <T : Any> infix(
            type: Class<T>,
            vararg keys: String,
            namespace: String = "common",
            executor: InfixContext.(T) -> Any?,
        ) {
            AsahiManager.getNamespace(namespace).getInfix(type).infix(*keys) { executor(this, it) }
        }
    }
}