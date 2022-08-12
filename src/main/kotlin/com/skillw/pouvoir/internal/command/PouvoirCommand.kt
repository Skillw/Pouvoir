package com.skillw.pouvoir.internal.command

import com.skillw.pouvoir.Pouvoir
import org.bukkit.command.CommandSender
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.console
import taboolib.common.platform.function.pluginVersion
import taboolib.common.platform.function.submit
import taboolib.common5.Mirror
import taboolib.module.chat.TellrawJson
import taboolib.module.chat.colored
import taboolib.module.lang.asLangText
import taboolib.module.lang.sendLang
import taboolib.platform.util.sendLang
import java.util.*

@CommandHeader(name = "pouvoir", aliases = ["pou"], permission = "pouvoir.command")
object PouvoirCommand {

    @CommandBody
    val main = mainCommand {
        execute<CommandSender> { sender, _, _ ->
            sender.sendLang("command-message")
        }
        incorrectCommand { sender, _, _, _ ->
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
            suggestion<ProxyCommandSender> { _, _ ->
                Pouvoir.scriptManager.map.map { it.key }
            }
            dynamic {
                suggestion<ProxyCommandSender> { _, context ->
                    (Pouvoir.scriptManager.search(context.argument(-1))
                        ?: return@suggestion emptyList()).annotationData.keys.toList()
                }
                execute<ProxyCommandSender> { sender, context, argument ->
                    submit(async = true) {
                        val function = argument.split(" ")[0]
                        val args =
                            argument.replace("$function ", "").split(" ").filter { it.isNotBlank() }.toList()
                                .toTypedArray()
                        if (args.isEmpty()) {
                            return@submit
                        }
                        val fileName = context.argument(-1)
                        val arguments = HashMap<String, Any>()
                        arguments["sender"] = sender
                        arguments["args"] = args
                        sender.sendLang("command-script-invoke", fileName)
                        Pouvoir.scriptManager.invoke<Any?>(
                            "$fileName::$function",
                            arguments = arguments,
                            sender = sender
                        )
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

    @CommandBody(permission = "pouvoir.command.report")
    val report = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            Mirror.report(sender)
        }
    }

    @CommandBody(permission = "pouvoir.command.clear")
    val clear = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            Mirror.mirrorData.clear()
            sender.sendLang("command-clear")
        }
    }

    @CommandBody(permission = "pouvoir.command.info")
    val info = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            sender.sendMessage("&aPouvoir &9v$pluginVersion &6By Glom_".colored())
        }
    }

    @CommandBody(permission = "pouvoir.command.pool")
    val task = subCommand {
        literal("info", optional = true) {
            execute<ProxyCommandSender> { sender, _, _ ->
                val messages = LinkedList<TellrawJson>()
                messages += TellrawJson().append(
                    ("&aPouvoir &9v$pluginVersion &6By Glom_ " + console().asLangText("command-task-info")).colored()
                )
                Pouvoir.scriptTaskManager.values.forEach { pool ->
                    messages.addAll(pool.info())
                }
                if (messages.size == 1) messages += TellrawJson().append(
                    console().asLangText("running-task-empty").colored()
                )
                messages.forEach {
                    it.sendTo(sender)
                }
            }
        }
        literal("stop", optional = true) {
            dynamic {
                suggestion<ProxyCommandSender> { _, _ ->
                    Pouvoir.scriptTaskManager.workingTasks
                }
                execute<ProxyCommandSender> { sender, _, argument ->
                    Pouvoir.scriptTaskManager.values.forEach {
                        if (!it.stop(argument)) return@forEach
                        sender.sendLang("command-stop-task", argument)
                        return@execute
                    }
                }
            }
        }
    }
}