package com.skillw.pouvoir.api.able

/**
 * ClassName : com.skillw.com.skillw.pouvoir.api.able.Cloneable
 * Created by Glom_ on 2021-04-10 22:46:16
 * Copyright  2021 user. All rights reserved.
 */
interface Cloneable<K, V> : Keyable<K> {
    fun clone(): V
}