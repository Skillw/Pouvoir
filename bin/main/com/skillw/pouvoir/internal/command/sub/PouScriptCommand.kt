package com.skillw.pouvoir.internal.command.sub

import com.skillw.asahi.api.member.lexer.AsahiDemand
import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.PouvoirAPI.placeholder
import com.skillw.pouvoir.internal.command.PouvoirCommand.soundClick
import com.skillw.pouvoir.internal.command.PouvoirCommand.soundSuccess
import com.skillw.pouvoir.util.timeSafe
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.submitAsync
import taboolib.module.lang.sendLang
import java.util.concurrent.atomic.AtomicInteger
import javax.script.SimpleBindings

internal object PouScriptCommand {
    val run = subCommand {
        dynamic("engine") {
            suggestion<ProxyCommandSender> { sender, _ ->
                sender.soundClick()
                Pouvoir.scriptEngineManager.keys.toList()
            }
            dynamic("path") {
                suggestion<ProxyCommandSender> { sender, context ->
                    sender.soundClick()
                    Pouvoir.scriptManager.values.filter { it.pouEngine.key == context["engine"] }.map { it.key }
                }
                dynamic("function") {
                    suggestion<ProxyCommandSender> { sender, context ->
                        sender.soundClick()
                        Pouvoir.scriptManager[context["path"]]!!.functions.toList()
                    }
                    execute<ProxyCommandSender> { sender, context, argument ->
                        submitAsync {
                            val path = context["path"]
                            val function = context["function"].split(" ")[0]
                            val after = argument.substring(function.length)
                            val demand =
                                AsahiDemand.of((sender.origin as? Player)?.let { after.placeholder(it) } ?: after)
                            val arguments = HashMap<String, Any>()
                            arguments["sender"] = sender.origin
                            arguments["@selector"] = sender.origin
                            demand.run {
                                arguments["args"] = args
                                arguments.putAll(demand)
                                tags.forEach { tag ->
                                    arguments[tag] = true
                                }
                            }
                            val parameters = demand.args.toTypedArray()
                            val silent = demand.tags.contains("silent")
                            if (!silent) {
                                sender.soundSuccess()
                                sender.sendLang("command-script-invoke", path, function)
                            }
                            Pouvoir.scriptManager.invoke<Any?>(
                                path, function,
                                arguments = arguments,
                                sender = if (!silent) sender else null,
                                parameters = parameters
                            )
                        }
                    }
                }
            }
        }
    }
    private var count = AtomicInteger(0)

    val eval = subCommand {
        dynamic("engine") {
            suggestion<ProxyCommandSender> { sender, _ ->
                sender.soundClick()
                Pouvoir.scriptEngineManager.keys.toList()
            }
            execute<ProxyCommandSender> { sender, context, argument ->
                submitAsync {
                    val engine = context["engine"].substringBefore(' ')
                    val script = argument.substring(engine.length)
                    val arguments = SimpleBindings()
                    arguments["sender"] = sender.origin
                    arguments["@selector"] = sender.origin
                    sender.soundSuccess()
                    val count = count.getAndIncrement()
                    sender.sendLang("command-script-eval", count)
                    val (result, duration) = timeSafe {
                        Pouvoir.scriptEngineManager[engine]!!.engine.eval(script, arguments)
                    }
                    sender.sendLang("command-script-eval-done", count, result.toString(), duration)
                }
            }
        }
    }
}