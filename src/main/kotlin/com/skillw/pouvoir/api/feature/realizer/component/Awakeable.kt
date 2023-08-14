package com.skillw.pouvoir.api.feature.realizer.component


/**
 * @className Realizable
 *
 * @author Glom
 * @date 2023/1/5 16:25 Copyright 2022 user. All rights reserved.
 */
interface Awakeable {
    fun onLoad() {}
    fun onEnable() {}
    fun onActive() {}
    fun onReload() {}

    fun onDisable() {}
}