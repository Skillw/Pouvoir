package com.skillw.pouvoir.internal.core.function.functions.common.str

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parser.Parser
import taboolib.common5.Coerce
import taboolib.common5.util.createBar


@AutoRegister
object FunctionCreateBar : PouFunction<String>(
    "createBar"
) {
    override fun execute(parser: Parser): String {
        with(parser) {
//                empty: String, fill: String, length: Int, percent: Double
            val map = parseMap()
            val empty = map["empty"]?.toString() ?: "&7|"
            val fill = map["fill"]?.toString() ?: "&a|"
            val length = Coerce.toInteger(map["length"] ?: 20)
            val percent = Coerce.toDouble(map["percent"] ?: 0.0)
            return createBar(empty, fill, length, percent)
        }
    }
}



