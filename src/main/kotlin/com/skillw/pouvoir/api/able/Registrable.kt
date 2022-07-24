package com.skillw.pouvoir.api.able

/**
 * @className Registrable
 * @author Glom
 * @date 2022/7/18 7:56
 * Copyright  2022 user. All rights reserved.
 */
interface Registrable<K> : Keyable<K> {
    fun register()
}