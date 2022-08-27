package com.skillw.pouvoir.internal.core.function.action

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.action.IAction
import com.skillw.pouvoir.api.function.parser.Parser
import org.bukkit.event.Cancellable

/**
 * @className ActionMap
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AutoRegister
object ActionCancellable : IAction {
    override val actions: Set<String> =
        hashSetOf("isCancelled")
    override val type: Class<*> = Cancellable::class.java

    override fun action(parser: Parser, obj: Any, action: String): Any? {
        if (obj !is Cancellable) error("$obj is not a Cancellable")
        with(parser) {
            when (action) {
                "isCancelled" -> {
                    if (except("to")) {
                        obj.isCancelled = parseBoolean()
                    }
                    return obj.isCancelled
                }

                else -> {
                    error("unknown Cancellable $obj action $action")
                }
            }
        }
    }

}