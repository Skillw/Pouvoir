package com.skillw.pouvoir.api.feature.database

import com.skillw.pouvoir.api.plugin.map.component.Keyable

/**
 * @className BaseContainer
 *
 * 数据库容器抽象基类，拥有独立生命周期
 *
 * 等同于SQL数据库中的一个Table 或者 Redis中的SingleRedisConnection
 *
 * @author Glom
 * @date 2023/1/12 18:35 Copyright 2024 Glom. 
 */
abstract class BaseContainer(
    override val key: String,
    val holder: ContainerHolder<*>,
) : Keyable<String> {
    open fun onEnable() {}
    open fun onActive() {}
    open fun onReload() {}
    open fun onDisable() {}
}