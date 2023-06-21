package com.skillw.pouvoir.api.feature.placeholder

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.plugin.SubPouvoir
import com.skillw.pouvoir.api.plugin.map.component.Registrable
import org.bukkit.entity.LivingEntity


/**
 * PouPlaceholder
 *
 * @param identifier 标识符
 * @constructor
 * @property name 插件名
 * @property author 作者
 * @property version 版本
 */
abstract class PouPlaceHolder(
    identifier: String,
    val name: String,
    val author: String,
    val version: String,
) : Registrable<String> {

    constructor(identifier: String, subPouvoir: SubPouvoir) : this(
        identifier,
        subPouvoir.plugin.name,
        subPouvoir.plugin.description.authors.toString(),
        subPouvoir.plugin.description.version
    )

    final override val key: String = identifier

    /**
     * 解析变量
     *
     * @param params 参数
     * @param entity 实体
     * @param def 默认值
     * @return 返回值
     */
    abstract fun onPlaceHolderRequest(params: String, entity: LivingEntity, def: String): String?
    override fun register() {
        Pouvoir.placeholderManager.register(key, this)
    }
}