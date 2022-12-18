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
object ParserInt : TokenParser(Int::class.java) {
    override fun parse(parser: Parser): Any? =
        Coerce.asInteger(ParserString.parse(parser)).run { if (isPresent) get() else null }
}

@AutoRegister
object ParserLong : TokenParser(Long::class.java) {
    override fun parse(parser: Parser): Any? =
        Coerce.asLong(ParserString.parse(parser)).run { if (isPresent) get() else null }
}

@AutoRegister
object ParserFloat : TokenParser(Float::class.java) {
    override fun parse(parser: Parser): Any? =
        Coerce.asFloat(ParserString.parse(parser)).run { if (isPresent) get() else null }
}

@AutoRegister
object ParserDouble : TokenParser(Double::class.java) {
    override fun parse(parser: Parser): Double? =
        Coerce.asDouble(ParserString.parse(parser)).run { if (isPresent) get() else null }
}

@AutoRegister
object ParserChar : TokenParser(Char::class.java) {
    override fun parse(parser: Parser): Any? = parser.parseString().first()
}

@AutoRegister
object ParserByte : TokenParser(Byte::class.java) {
    override fun parse(parser: Parser): Any? =
        Coerce.asByte(ParserString.parse(parser)).run { if (isPresent) get() else null }
}

@AutoRegister
object ParserShort : TokenParser(Short::class.java) {
    override fun parse(parser: Parser): Any? =
        Coerce.asShort(ParserString.parse(parser)).run { if (isPresent) get() else null }
}

@AutoRegister
object ParserBoolean : TokenParser(Boolean::class.java) {
    override fun parse(parser: Parser): Any? =
        Coerce.asBoolean(ParserString.parse(parser)).run { if (isPresent) get() else null }
}
