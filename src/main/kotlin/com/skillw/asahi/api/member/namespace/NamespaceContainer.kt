package com.skillw.asahi.api.member.namespace

import com.skillw.asahi.api.AsahiManager

/**
 * @className NamespaceContainer
 *
 * @author Glom
 * @date 2023/1/27 16:11 Copyright 2024 Glom.
 */
class NamespaceContainer(private val namespaces: LinkedHashSet<Namespace> = LinkedHashSet()) :
    Set<Namespace> by namespaces {

    init {
        AsahiManager.loadSharedNamespace(this)
    }

    fun namespaceNames(): Array<String> = namespaces.map { it.key }.toTypedArray()

    /**
     * 添加命名空间
     *
     * @param names 命名空间id
     * @return 自身
     */
    fun addNamespaces(vararg names: String) {
        addNamespaces(AsahiManager.getNamespaces(*names))
    }

    /**
     * 删除命名空间
     *
     * @param names 命名空间id
     * @return 自身
     */
    fun removeSpaces(vararg names: String) {
        removeSpaces(AsahiManager.getNamespaces(*names))
    }

    /**
     * 添加命名空间
     *
     * @param namespaces 命名空间
     * @return 自身
     */
    fun addNamespaces(vararg namespaces: Namespace) {
        addNamespaces(namespaces.toSet())
    }

    /**
     * 删除命名空间
     *
     * @param namespaces 命名空间
     * @return 自身
     */
    fun removeSpaces(vararg namespaces: Namespace) {
        removeSpaces(namespaces.toSet())
    }

    /**
     * 添加命名空间
     *
     * @param namespaces 命名空间
     * @return 自身
     */
    fun addNamespaces(namespaces: Collection<Namespace>) {
        this.namespaces.addAll(namespaces)
        this.namespaces.sorted()
    }

    /**
     * 删除命名空间
     *
     * @param namespaces 命名空间
     * @return 自身
     */
    fun removeSpaces(namespaces: Collection<Namespace>) {
        this.namespaces.removeAll(namespaces.toSet())
        this.namespaces.sorted()
    }
}