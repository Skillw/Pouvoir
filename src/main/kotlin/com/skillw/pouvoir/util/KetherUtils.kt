package com.skillw.pouvoir.util

import com.skillw.pouvoir.Pouvoir
import org.bukkit.command.CommandSender
import taboolib.common.platform.function.adaptCommandSender
import taboolib.module.kether.KetherShell
import java.util.function.Function

object KetherUtils {
    @JvmStatic
    fun invokeString(
        kether: String,
        argsMap: MutableMap<String, Any>,
        sender: CommandSender? = null
    ): Any? {

        return KetherShell.eval(
            Pouvoir.pouPlaceHolderAPI.replace(
                null,
                kether.replace(
                    "kether::",
                    ""
                )
            )
        ) {
            this["example"] = Function<String, String> { it.toString() }

            if (sender != null)
                this.sender = adaptCommandSender(sender)
            argsMap.forEach {
                this[it.key] = it.value
            }
        }.get()
    }
}