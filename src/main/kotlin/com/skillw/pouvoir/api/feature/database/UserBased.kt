package com.skillw.pouvoir.api.feature.database

/**
 * @className UserBased
 *
 * 基于用户的容器抽象类
 *
 * 其容器特征有：
 * - 列名只有 username key value
 * - 一一对应，username key不可重复
 *
 * @author Glom
 * @date 2023/1/12 21:54 Copyright 2024 Glom. 
 */
interface UserBased {
    /**
     * 获取本身
     *
     * @return
     */
    fun database(): UserBased {
        return this
    }

    /**
     * 获取数据
     *
     * @param user 用户名
     * @param key 键名
     * @return 值
     */
    operator fun get(user: String, key: String): String?

    /**
     * 删除数据
     *
     * @param user 用户名
     * @param key 键名
     */
    fun delete(user: String, key: String)

    /**
     * 设置数据
     *
     * @param user 用户名
     * @param key 键名
     * @param value 值
     */
    operator fun set(user: String, key: String, value: String?)

    /**
     * 是否存在此数据
     *
     * @param user 用户名
     * @param key 键名
     * @return 是否存在
     */
    fun contains(user: String, key: String): Boolean

}