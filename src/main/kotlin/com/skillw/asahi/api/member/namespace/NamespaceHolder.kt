package com.skillw.asahi.api.member.namespace

import com.skillw.asahi.api.AsahiManager
import com.skillw.asahi.api.member.parser.prefix.namespacing.BasePrefix

/**
 * @className NamespaceHolder
 *
 * @author Glom
 * @date 2023/1/19 16:48 Copyright 2023 user. All rights reserved.
 */
interface NamespaceHolder<T : NamespaceHolder<T>> {
    /** 持有的命名空间 */
    val namespaces: MutableSet<Namespace>

    /**
     * 获取命名空间的名称数组
     *
     * @return
     */
    fun namespaceNames(): Array<String> = namespaces.map { it.key }.toTypedArray()

    /**
     * 添加命名空间
     *
     * @param names 命名空间id
     * @return 自身
     */
    fun addNamespaces(vararg names: String): T {
        namespaces.addAll(AsahiManager.getNamespaces(*names))
        return this as T
    }

    /**
     * 删除命名空间
     *
     * @param names 命名空间id
     * @return 自身
     */
    fun removeSpaces(vararg names: String): T {
        namespaces.removeAll(AsahiManager.getNamespaces(*names))
        return this as T
    }

    /**
     * 是否有前缀解释器
     *
     * @param token token
     * @return 是否有前缀解释器
     */
    fun hasPrefix(token: String): Boolean {
        return namespaces.any { it.hasPrefix(token) }
    }

    /**
     * 获取前缀解释器
     *
     * @param token token
     * @return 前缀解释器
     */
    fun getPrefix(token: String): List<BasePrefix<*>> {
        return namespaces.filter { it.hasPrefix(token) }.map { it.getPrefix(token)!! }
    }
}