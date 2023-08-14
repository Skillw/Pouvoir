package com.skillw.pouvoir.internal.command

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.internal.command.sub.PouMirrorCommand
import com.skillw.pouvoir.internal.command.sub.PouScriptCommand
import com.skillw.pouvoir.internal.manager.PouConfig
import com.skillw.pouvoir.util.soundClick
import com.skillw.pouvoir.util.soundFail
import com.skillw.pouvoir.util.soundSuccess
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.pluginVersion
import taboolib.module.chat.colored
import taboolib.module.lang.sendLang
import taboolib.platform.util.sendLang

@CommandHeader(name = "pouvoir", aliases = ["pou"], permission = "pouvoir.command")
internal object PouvoirCommand {
    //哥们别骂 TLib6的castSafely有问题，只能这样
    internal fun ProxyCommandSender.soundSuccess() {
        (this.origin as? Player?)?.soundSuccess()
    }

    internal fun ProxyCommandSender.soundFail() {
        (this.origin as? Player?)?.soundFail()
    }

    internal fun ProxyCommandSender.soundClick() {
        (this.origin as? Player?)?.soundClick()
    }

    @CommandBody
    val main = mainCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            sender.sendLang("command-message")
            sender.soundSuccess()
        }
        incorrectCommand { sender, _, _, _ ->
            sender.sendLang("wrong-command-message")
            sender.soundFail()
        }
    }

    @CommandBody(permission = "pouvoir.command.debug")
    val debug = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            sender.soundSuccess()
            PouConfig.debugMode = !PouConfig.debugMode
            if (PouConfig.debugMode)
                sender.sendLang("command-debug-on")
            else
                sender.sendLang("command-debug-off")
        }
    }

    @CommandBody(permission = "pouvoir.command.reload")
    val reload = subCommand {
        execute<CommandSender> { sender, _, _ ->
            (sender as? Player?)?.soundSuccess()
            sender.sendLang("command-reload")
            Pouvoir.reload()
        }
    }

    @CommandBody(permission = "pouvoir.command.help")
    val help = subCommand {
        execute<CommandSender> { sender, _, _ ->
            (sender as? Player?)?.soundSuccess()
            sender.sendLang("command-message")
        }
    }

    @CommandBody(permission = "pouvoir.command.info")
    val info = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            sender.soundSuccess()
            sender.sendMessage("&aPouvoir &9v$pluginVersion &6By Glom_".colored())
        }
    }

    @CommandBody(permission = "pouvoir.command.report")
    val report = PouMirrorCommand.report

    @CommandBody(permission = "pouvoir.command.clear")
    val clear = PouMirrorCommand.clear

    @CommandBody(permission = "pouvoir.command.run")
    val run = PouScriptCommand.run

    @CommandBody(permission = "pouvoir.command.eval")
    val eval = PouScriptCommand.eval


}