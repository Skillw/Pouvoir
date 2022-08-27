package com.skillw.pouvoir.internal.core.function.parser

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.parser.Parser
import com.skillw.pouvoir.api.function.parser.TokenParser
import taboolib.common5.Coerce

/**
 * @className BasicTypeParsers
 *
 * @author Glom
 * @date 2022/8/17 11:10 Copyright 2022 user. All rights reserved.
 */

@AutoRegister
object IntParser : TokenParser(Int::class.java) {
    override fun parse(parser: Parser): Any? =
        Coerce.asInteger(StringParser.parse(parser)).run { if (isPresent) get() else null }
}

@AutoRegister
object LongParser : TokenParser(Long::class.java) {
    override fun parse(parser: Parser): Any? =
        Coerce.asLong(StringParser.parse(parser)).run { if (isPresent) get() else null }
}

@AutoRegister
object FloatParser : TokenParser(Float::class.java) {
    override fun parse(parser: Parser): Any? =
        Coerce.asFloat(StringParser.parse(parser)).run { if (isPresent) get() else null }
}

@AutoRegister
object DoubleParser : TokenParser(Double::class.java) {
    override fun parse(parser: Parser): Double? =
        Coerce.asDouble(StringParser.parse(parser)).run { if (isPresent) get() else null }
}

@AutoRegister
object CharParser : TokenParser(Char::class.java) {
    override fun parse(parser: Parser): Any? = parser.parseString().first()
}

@AutoRegister
object ByteParser : TokenParser(Byte::class.java) {
    override fun parse(parser: Parser): Any? =
        Coerce.asByte(StringParser.parse(parser)).run { if (isPresent) get() else null }
}

@AutoRegister
object ShortParser : TokenParser(Short::class.java) {
    override fun parse(parser: Parser): Any? =
        Coerce.asShort(StringParser.parse(parser)).run { if (isPresent) get() else null }
}

@AutoRegister
object BooleanParser : TokenParser(Boolean::class.java) {
    override fun parse(parser: Parser): Any? =
        Coerce.asBoolean(StringParser.parse(parser)).run { if (isPresent) get() else null }
}
