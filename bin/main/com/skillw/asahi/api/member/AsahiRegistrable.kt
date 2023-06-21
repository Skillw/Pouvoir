package com.skillw.asahi.api.member

/**
 * @className Registrable
 *
 * @author Glom
 * @date 2022/7/18 7:56 Copyright 2022 user. All rights reserved.
 *
 * 可注册的类
 */
interface AsahiRegistrable<K> {
    /** 键 */
    val key: K
    fun register()
}