package com.skillw.pouvoir.internal.feature.selector

import com.skillw.asahi.util.castSafely
import com.skillw.pouvoir.api.feature.selector.EntityFlag
import com.skillw.pouvoir.api.feature.selector.SimpleSelector
import com.skillw.pouvoir.api.feature.selector.Target
import com.skillw.pouvoir.api.feature.selector.toTarget
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import com.skillw.pouvoir.api.plugin.map.DataMap

/**
 * Flag
 *
 * 标签选择器
 *
 * #选取带有 my_flag 标签的实体 且距离Caster 10格以内
 *
 * set result to selector &caster [ @flag 'my_flag' @range 10 ]
 *
 * for entity in &result { &caster damage &entity &damage }
 *
 * @constructor Create empty Flag
 */
@AutoRegister
object Flag : SimpleSelector("flag") {
    override fun SelectorContext.getTargets(caster: Target): Collection<Target> {
        val key = get("key", "")
        return EntityFlag.getEntities(key).map { it.toTarget() }
    }

    override fun addParameter(dataMap: DataMap, vararg args: Any?) {
        dataMap["key"] = args.getOrNull(0).castSafely<String>() ?: 10.0
    }
}