package com.skillw.pouvoir.internal.function.functions.common.str

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.parse.Parser
import java.util.*


@AutoRegister
object FunctionJoin : PouFunction<String>(
    "join"
) {
    override fun execute(parser: Parser): String? {
        with(parser) {
            val list = LinkedList<String>()
            if (!except("[")) return null
            while (hasNext()) {
                val any = parseAny()
                list.add(any.toString())
                if (except("]")) break
            }
            if (!except("by")) return null
            val by = parseString()
            return list.joinToString(by)
        }
    }
}



