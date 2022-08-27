package com.skillw.pouvoir.internal.core.function.functions.common.bukkit

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser
import org.bukkit.Location
import taboolib.common.platform.ProxyParticle
import taboolib.common.platform.sendTo
import taboolib.common.util.Vector
import taboolib.platform.util.toProxyLocation

/**
 * @className FunctionBlock
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AutoRegister
object FunctionEffect : PouFunction<Unit>(
    "effect"
) {
    override fun execute(parser: Parser) {
        with(parser) {
            val particle = parse<ProxyParticle>()
            except("at")
            val location = parse<Location>().toProxyLocation()
            var range = 15.0
            var offset = Vector(0, 0, 0)
            var count = 1
            var speed = 1.0
            var data: ProxyParticle.Data? = null
            if (except("limit")) {
                range = parseDouble()
            }
            if (except("offset")) {
                offset = parse()
            }
            if (except("amount")) {
                count = parseInt()
            }
            if (except("speed")) {
                speed = parseDouble()
            }
            if (except("data")) {
                data = parse()
            }
            particle.sendTo(location, range, offset, count, speed, data)
        }
    }
}