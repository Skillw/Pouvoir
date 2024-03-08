package com.skillw.pouvoir.api.plugin.`object`

/**
 * @className Releasable
 *
 * @author Glom
 * @date 2023/1/22 22:56 Copyright 2024 Glom. 
 */
interface Releasable {
    /** 是否在重载时注销 */
    var release: Boolean

    fun unregister()
}