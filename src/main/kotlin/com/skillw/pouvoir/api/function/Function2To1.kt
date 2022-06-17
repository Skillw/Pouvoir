package com.skillw.pouvoir.api.function

/**
 * @className Function2To1
 * @author Glom
 * @date 2022/6/12 12:41
 * Copyright  2022 user. All rights reserved.
 */
fun interface Function2To1<T, V, R> {
    fun invoke(t: T, v: V): R
}