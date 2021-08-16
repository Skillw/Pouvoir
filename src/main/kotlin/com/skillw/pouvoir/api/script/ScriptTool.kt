package com.skillw.pouvoir.api.script

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.Pouvoir.listenerManager
import com.skillw.pouvoir.Pouvoir.scriptManager
import com.skillw.pouvoir.api.listener.ScriptListener
import com.skillw.pouvoir.util.ClassUtils
import com.skillw.pouvoir.util.MessageUtils.wrong
import com.skillw.rpglib.api.map.BaseMap
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.command.*
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import taboolib.platform.BukkitCommand

object ScriptTool : BaseMap<String, Any>() {
    @JvmStatic
    fun runTask(task: () -> Unit) {
        object : BukkitRunnable() {
            override fun run() {
                task.invoke()
            }
        }.runTask(Pouvoir.plugin)
    }

    @JvmStatic
    fun placeHolder(identifier: String, author: String, version: String, path: String) {
        object : PlaceholderExpansion() {
            override fun getIdentifier(): String {
                return identifier
            }

            override fun getAuthor(): String {
                return author
            }

            override fun getVersion(): String {
                return version
            }

            override fun onRequest(player: OfflinePlayer, params: String): String {
                return scriptManager.invokePathWithFunction(path, player, params).toString()
            }
        }.register()
    }

    @JvmStatic
    fun addListener(
        key: String,
        path: String,
        eventPriority: String = "NORMAL",
        ignoreCancel: Boolean = false,
        exec: (Event) -> Unit
    ) {
        try {
            val clazz = Class.forName(path)
            val priority = try {
                EventPriority.valueOf(eventPriority)
            } catch (e: Exception) {
                EventPriority.NORMAL
            }
            addListener(key, clazz as Class<out Event>, priority, ignoreCancel, exec)
        } catch (e: Exception) {
            wrong("The class $path dose not exist / is not a Event");
        }
    }

    @JvmStatic
    fun addListener(
        key: String,
        event: Class<out Event>,
        eventPriority: EventPriority = EventPriority.NORMAL,
        ignoreCancel: Boolean = false,
        exec: (Event) -> Unit
    ) {
        val scriptListener = ScriptListener.build(key, event, eventPriority, ignoreCancel, exec)
        scriptListener.register()
    }

    @JvmStatic
    fun removeListener(key: String) {
        listenerManager.removeByKey(key)
    }

    @JvmStatic
    fun isPluginEnabled(pluginName: String): Boolean {
        return Bukkit.getPluginManager().isPluginEnabled(pluginName)
    }

    @JvmStatic
    fun getPlugin(pluginName: String): Plugin? {
        return Bukkit.getPluginManager().getPlugin(pluginName)
    }

    @JvmStatic
    fun static(className: String): Any? {
        return staticClass(className)
    }

    @JvmStatic
    fun staticClass(className: String): Any? {
        return ClassUtils.staticClass(className)
    }

    @JvmStatic
    fun commandExec(exec: (CommandSender, Command, String, Array<String>) -> Boolean = { _, _, _, _ -> false }): CommandExecutor {
        return CommandExecutor { commandSender, command, s, strings ->
            return@CommandExecutor exec.invoke(commandSender, command, s, strings)
        }
    }

    @JvmStatic
    fun commandTab(
        tab: (CommandSender, Command, String, Array<String>) ->
        MutableList<String> = { _, _, _, _ -> emptyList<String>().toMutableList() }
    ): TabCompleter {
        return TabCompleter() { commandSender, command, s, strings ->
            return@TabCompleter tab.invoke(
                commandSender,
                command,
                s,
                strings
            )
        }
    }

    @JvmStatic
    fun command(
        name: String
    ): PluginCommand {
        return BukkitCommand().constructor.newInstance(name, Pouvoir.plugin)
    }

    @JvmStatic
    fun regCommand(command: PluginCommand) {
        command.register(BukkitCommand().commandMap)
    }

    @JvmStatic
    fun unRegCommand(name: String) {
        BukkitCommand().unregisterCommand(name)
    }

}