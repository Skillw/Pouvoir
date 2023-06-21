package com.skillw.pouvoir.internal.command.sub

import com.skillw.pouvoir.internal.command.PouvoirCommand.soundSuccess
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.subCommand
import taboolib.common5.Mirror
import taboolib.module.lang.sendLang

internal object PouMirrorCommand {
    val report = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            sender.soundSuccess()
            Mirror.report(sender)
        }
    }

    val clear = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            sender.soundSuccess()
            Mirror.mirrorData.clear()
            sender.sendLang("command-clear")
        }
    }
}