package com.skillw.pouvoir.internal.command

import com.skillw.pouvoir.Pouvoir
import org.bukkit.command.CommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.subCommand
import taboolib.module.lang.sendLang
import taboolib.platform.util.sendLang
import java.util.concurrent.ConcurrentHashMap

@CommandHeader(name = "pouvoir", aliases = ["pou"], permission = "pouvoir.command")
object PouvoirCommand {

    @CommandBody
    val main = mainCommand {
        execute<CommandSender> { sender, _, _ ->
            sender.sendLang("command-message")
        }
        incorrectCommand { sender, context, index, state ->
            sender.sendLang("wrong-command-message")
        }
    }

    fun Array<String>.sendMessage(sender: CommandSender) {
        this.forEach {
            sender.sendMessage(it)
        }
    }

    @CommandBody(permission = "pouvoir.command.js")
    val run = subCommand {
        dynamic {
            suggestion<CommandSender>(uncheck = true) { _, _ ->
                Pouvoir.scriptManager.map { it.key }
            }
            dynamic {
                suggestion<CommandSender> { _, context ->
                    (Pouvoir.scriptManager[context.argument(-1)]
                        ?: return@suggestion emptyList()).annotationData.keys.toList()
                }
                execute<CommandSender> { sender, context, argument ->
                    val function = argument.split(" ")[0]
                    val args =
                        argument.replace("$function ", "").split(" ").filter { it.isNotBlank() }.toList().toTypedArray()
                    if (args.isEmpty()) {
                        return@execute
                    }
                    val fileName = context.argument(-1)
                    val argsMap = ConcurrentHashMap<String, Any>()
                    argsMap["sender"] = sender
                    argsMap["args"] = args
                    sender.sendLang("command-script-invoke", fileName)
                    Pouvoir.scriptManager.invoke<Unit>("$fileName::$function", variables = argsMap)
                }
            }
        }
    }

    @CommandBody(permission = "pouvoir.command.reload")
    val reload = subCommand {
        execute<CommandSender> { sender, _, _ ->
            sender.sendLang("command-reload")
            Pouvoir.reload()
        }
    }

    @CommandBody(permission = "pouvoir.command.help")
    val help = subCommand {
        execute<CommandSender> { sender, _, _ ->
            sender.sendLang("command-message")
        }
    }


}