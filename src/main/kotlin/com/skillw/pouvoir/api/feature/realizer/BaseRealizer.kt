package com.skillw.pouvoir.api.feature.realizer

import com.skillw.pouvoir.api.plugin.map.component.Registrable
import java.io.File

/**
 * @className BaseRealizer
 *
 * @author Glom
 * @date 2023/1/5 10:46 Copyright 2022 user. All rights reserved.
 */
abstract class BaseRealizer(final override val key: String) : Registrable<String> {

    //配置文件
    abstract val file: File
    abstract val manager: BaseRealizerManager

    // 配置
    val config = HashMap<String, Any>()

    // 每次重载时会补充缺失节点与注释，会忽略此集合中的节点
    val ignorePaths = HashSet<String>()

    override fun register() {
        manager.register(this)
    }

    override fun toString(): String {
        return "BaseRealizer $key in ${manager.key}"
    }
}