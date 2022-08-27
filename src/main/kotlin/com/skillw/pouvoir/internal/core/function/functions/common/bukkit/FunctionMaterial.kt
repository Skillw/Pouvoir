package com.skillw.pouvoir.internal.core.function.functions.common.bukkit

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser
import org.bukkit.Material
import taboolib.library.xseries.XMaterial

/**
 * @className FunctionBlock
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AutoRegister
object FunctionMaterial : PouFunction<Material>(
    "material"
) {
    override fun execute(parser: Parser): Material {
        with(parser) {
            val token = parseString()
            return XMaterial.matchXMaterial(token)
                .run { if (this.isPresent) get().parseMaterial() else Material.matchMaterial(token) }
                ?: error("Material not found: $token")
        }
    }
}