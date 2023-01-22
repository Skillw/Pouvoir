package com.skillw.pouvoir.internal.feature.selector

import com.skillw.pouvoir.api.feature.selector.SimpleSelector
import com.skillw.pouvoir.api.feature.selector.Target
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import com.skillw.pouvoir.api.plugin.map.DataMap

@AutoRegister
object Self : SimpleSelector("self") {
    override fun SelectorContext.getTargets(caster: Target): Collection<Target> {
        return setOf(caster)
    }

    override fun addParameter(dataMap: DataMap, vararg args: Any?) {

    }
}