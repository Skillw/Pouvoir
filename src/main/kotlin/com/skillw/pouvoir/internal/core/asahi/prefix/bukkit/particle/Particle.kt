package com.skillw.pouvoir.internal.core.asahi.prefix.bukkit.particle

import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.member.quest.Quester
import com.skillw.asahi.api.prefixParser
import com.skillw.asahi.api.quest
import com.skillw.asahi.api.quester
import org.bukkit.Location
import taboolib.common.platform.ProxyParticle
import taboolib.common.platform.sendTo
import taboolib.common.util.Vector
import taboolib.common5.cdouble
import taboolib.common5.cint
import taboolib.platform.util.toProxyLocation

/**
 * @className Effect
 *
 * @author Glom
 * @date 2023/1/14 0:13 Copyright 2023 user. All rights reserved.
 */

@AsahiPrefix(["particle"])
private fun particle() = prefixParser<Unit> {
    //粒子类型
    val particle = quest<ProxyParticle>()
    expect("at")
    //坐标
    val location = quest<Location>()
    val map = if (peek() == "[" || peek() == "{") questTypeMap() else quester { emptyMap<String, Any>() }
    //可见范围
    val range = map.quester { it.getOrDefault("range", 32.0).cdouble }
    //偏移
    val offset = map.quester { it.getOrDefault("offset", Vector(0, 0, 0)) as Vector }
    //数量
    val count = map.quester { it.getOrDefault("count", 1).cint }
    //速度
    val speed = map.quester { it.getOrDefault("speed", 1.0).cdouble }
    //粒子数据
    val data: Quester<ProxyParticle.Data?> = map.quester { it["data"] as? ProxyParticle.Data? }
    result {
        particle.get()
            .sendTo(
                location.get().toProxyLocation(),
                range.get(),
                offset.get(),
                count.get(),
                speed.get(),
                data.get()
            )
    }
}

//FIXME 这里要写粒子库对接
@AsahiPrefix(["effect"])
private fun effect() = prefixParser<Unit> {
    result { }
}