package com.skillw.pouvoir.internal.core.function.functions.common.bukkit

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser
import org.bukkit.entity.Entity
import java.util.*

/**
 * @className FunctionBlock
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AutoRegister
object FunctionUUID : PouFunction<UUID>(
    "uuid"
) {
    override fun execute(parser: Parser): UUID {
        with(parser) {
            return parse<Entity>().uniqueId
        }
    }
}