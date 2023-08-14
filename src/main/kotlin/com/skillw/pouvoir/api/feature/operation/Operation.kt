package com.skillw.pouvoir.api.feature.operation

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.plugin.map.component.Registrable

/**
 * @className NumberOperation
 *
 * @author Glom
 * @date 2022/7/19 12:38 Copyright 2022 user. All rights reserved.
 */
interface Operation<T> : Registrable<String> {
    /** 是否在重载时删除 */
    var release: Boolean

    /**
     * 做运算
     *
     * @param a 对象a
     * @param b 对象b
     * @return 运算结果
     */
    fun operate(a: T, b: T): T
    override fun register() {
        Pouvoir.operationManager.register(this)
    }
}