package com.skillw.pouvoir.internal.core.function.functions.common.bukkit

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser
import taboolib.common.platform.ProxyParticle
import taboolib.common5.Coerce

/**
 * @className FunctionBlock
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AutoRegister
object FunctionParticle : PouFunction<ProxyParticle>(
    "particle"
) {
    override fun execute(parser: Parser): ProxyParticle {
        with(parser) {
            val token = parseString()
            return Coerce.toEnum(token, ProxyParticle::class.java) ?: error("Wrong Particle Type $token")
        }
    }
}