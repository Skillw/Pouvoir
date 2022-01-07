package com.skillw.pouvoir.command

import com.skillw.pouvoir.Pouvoir
import org.bukkit.command.CommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.subCommand
import taboolib.platform.util.sendLang
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
    val js = subCommand {
        dynamic(optional = true) {
            suggestion<CommandSender>(uncheck = true) { _, _ ->
                Pouvoir.scriptManager.keys.toList()
            }
            execute<CommandSender> { sender, _, argument ->
                val args =
                    argument.replace("js ", "").split(" ").filter { it.isNotBlank() }.toList().toTypedArray()
                if (args.isEmpty()) {
                    return@execute
                }
                val fileName = args[0]
                val argsNew = arrayOfNulls<String>(args.size - 1)
                for (i in 1 until args.size) {
                    argsNew[i - 1] = args[i]
                }
                val argsMap: MutableMap<String, Any> = ConcurrentHashMap<String, Any>()
                argsMap["sender"] = sender
                argsMap["args"] = argsNew
                sender.sendLang("command-script-invoke", fileName)
                val result: Any? = Pouvoir.scriptManager.invokePathWithFunction(fileName, argsMap = argsMap)
                if (result != null) {
                    when (result.toString()) {
                        "wrong", "!wrong!" -> sender.sendLang("command-script-wrong", fileName)
                        "!wrong-format!" -> sender.sendLang("script-invoke-wrong-format")
                        else -> {
                        }
                    }
                }
            }
        }
    }

    @CommandBody(permission = "pouvoir.command.reload")
    val reload = subCommand {
        execute<CommandSender> { sender, _, _ ->
            Pouvoir.reloadAll()
            sender.sendLang("command-reload")
        }
    }

    @CommandBody(permission = "pouvoir.command.help")
    val help = subCommand {
        execute<CommandSender> { sender, _, _ ->
            sender.sendLang("command-message")
        }
    }


}