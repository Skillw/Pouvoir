package com.skillw.pouvoir.internal.command

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.Pouvoir.messagerBuilderManager
import com.skillw.pouvoir.Pouvoir.personalManager
import com.skillw.pouvoir.util.PlayerUtils.soundClick
import com.skillw.pouvoir.util.PlayerUtils.soundFail
import com.skillw.pouvoir.util.PlayerUtils.soundSuccess
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.pluginVersion
import taboolib.common.platform.function.submit
import taboolib.common5.Mirror
import taboolib.module.chat.colored
import taboolib.module.lang.sendLang
import taboolib.platform.util.sendLang

@CommandHeader(name = "pouvoir", aliases = ["pou"], permission = "pouvoir.command")
object PouvoirCommand {
    private fun ProxyCommandSender.soundSuccess() {
        (this.origin as? Player?)?.soundSuccess()
    }

    private fun ProxyCommandSender.soundFail() {
        (this.origin as? Player?)?.soundFail()
    }

    private fun ProxyCommandSender.soundClick() {
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

    @CommandBody(permission = "pouvoir.command.run")
    val run = subCommand {
        dynamic {
            suggestion<ProxyCommandSender> { sender, _ ->
                sender.soundClick()
                Pouvoir.scriptManager.map.map { it.key }
            }
            dynamic {
                suggestion<ProxyCommandSender> { sender, context ->
                    sender.soundClick()
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
                        sender.soundSuccess()
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

    @CommandBody(permission = "pouvoir.command.report")
    val report = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            sender.soundSuccess()
            Mirror.report(sender)
        }
    }

    @CommandBody(permission = "pouvoir.command.clear")
    val clear = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            sender.soundSuccess()
            Mirror.mirrorData.clear()
            sender.sendLang("command-clear")
        }
    }

    @CommandBody(permission = "pouvoir.command.info")
    val info = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            sender.soundSuccess()
            sender.sendMessage("&aPouvoir &9v$pluginVersion &6By Glom_".colored())
        }
    }

    @CommandBody(permission = "pouvoir.command.message")
    val message = subCommand {
        dynamic {
            suggestion<Player> { sender, _ ->
                sender.soundClick()
                messagerBuilderManager.keys.toList()
            }
            execute<Player> { sender, _, argument ->
                sender.soundSuccess()
                val uuid = sender.uniqueId
                personalManager[uuid] = argument
                sender.sendMessage("&aPouvoir &9v$pluginVersion &6By Glom_".colored())
            }
        }
    }


}