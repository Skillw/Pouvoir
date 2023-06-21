package com.skillw.asahi.api.member.namespace

import com.skillw.asahi.api.AsahiManager

/**
 * @className Namespacing
 *
 * 在命名空间中储藏的类
 *
 * @author Glom
 * @date 2023/1/20 10:12 Copyright 2023 user. All rights reserved.
 */
interface Namespacing {
    /** 命名空间 */
    val namespace: String

    /**
     * 命名空间对象
     *
     * @return 命名空间对象
     */
    fun namespace(): Namespace {
        return AsahiManager.getNamespace(namespace)
    }
}