package com.skillw.pouvoir.api.container

import taboolib.module.database.Where

/**
 * Artifex taboolib.expansion.ContainerOperatorUnique
 *
 * Glom: 拿來小改一下
 *
 * @author 坏黑
 * @since 2022/5/25 00:35
 */
interface IContainerOperator<T> {
    /** 统一容器 */
    fun select(where: Where.() -> Unit): Map<String, Any?>
    fun select(vararg rows: String, where: Where.() -> Unit): Map<String, Any?>
    fun update(map: Map<String, Any?>, where: Where.() -> Unit)


    fun keys(unique: T): List<String>
    operator fun get(unique: T): Map<String, Any?>
    operator fun get(unique: T, vararg rows: String): Map<String, Any?>
    operator fun set(unique: T, map: Map<String, Any?>)
}
