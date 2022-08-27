package com.skillw.pouvoir.internal.core.function.functions.common.bukkit

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser
import org.bukkit.Location
import taboolib.common.platform.ProxyParticle
import taboolib.platform.util.toProxyLocation

/**
 * @className FunctionBlock
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AutoRegister
object FunctionDestination : PouFunction<ProxyParticle.VibrationData.Destination>(
    "destination"
) {
    override fun execute(parser: Parser): ProxyParticle.VibrationData.Destination {
        with(parser) {
            val token = parseString()
            return when (token) {
                "location" -> ProxyParticle.VibrationData.LocationDestination(parse<Location>().toProxyLocation())
                "entity" -> ProxyParticle.VibrationData.EntityDestination(parse())
                else -> error("Wrong Destination Type $token")
            }
        }
    }
}