package com.skillw.pouvoir.api.feature.realizer.component

import com.skillw.pouvoir.api.feature.realizer.BaseRealizer

import taboolib.common5.cbool

/**
 * @className Realizable
 *
 * @author Glom
 * @date 2023/1/5 16:25 Copyright 2022 user. 
 */
interface Switchable {

    val defaultEnable: Boolean

    fun isEnable(): Boolean = (this as? BaseRealizer)?.config?.get("enable")?.cbool ?: defaultEnable
    fun isDisable(): Boolean = !isEnable()

    fun whenEnable() {}
    fun whenDisable() {}

}