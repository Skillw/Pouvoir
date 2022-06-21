package com.skillw.pouvoir.internal.command

import com.skillw.pouvoir.Pouvoir
import org.bukkit.command.CommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.subCommand
import taboolib.platform.util.sendLang
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@CommandHeader(name = "pouvoir", aliases = ["pou"], permission = "pouvoir.command")
object PouvoirCommand {

    @CommandBody
    val main = mainCommand {
        execute<CommandSender> { sender, _, _ ->
            sender.sendLang("command-message")
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
                Pouvoir.scriptManager.values.map { it.key }
            }
            dynamic {
                suggestion<CommandSender>(uncheck = true) { _, context ->
                    val list = LinkedList<String>()
                    val compiledFile = Pouvoir.scriptManager[context.argument(-1)] ?: return@suggestion emptyList()
                    compiledFile.functions.forEach { function ->
                        list.add(function)
                    }
                    list
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
                    Pouvoir.scriptManager.invokePathWithFunction("$fileName::$function", argsMap = argsMap)?.run {
                        when (this.toString()) {
                            "wrong", "!wrong!" -> sender.sendLang("command-script-wrong", fileName)
                            "!wrong-format!" -> sender.sendLang("script-invoke-wrong-format")
                            else -> {
                            }
                        }
                    }
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