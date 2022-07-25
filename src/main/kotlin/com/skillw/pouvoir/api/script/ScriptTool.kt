package com.skillw.pouvoir.api.script

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.Pouvoir.containerManager
import com.skillw.pouvoir.Pouvoir.listenerManager
import com.skillw.pouvoir.Pouvoir.scriptManager
import com.skillw.pouvoir.api.annotation.ScriptTopLevel
import com.skillw.pouvoir.api.listener.Priority
import com.skillw.pouvoir.api.listener.ScriptListener
import com.skillw.pouvoir.api.map.BaseMap
import com.skillw.pouvoir.util.ClassUtils
import com.skillw.pouvoir.util.ClassUtils.findClass
import com.skillw.pouvoir.util.ItemUtils.toMutableMap
import jdk.nashorn.api.scripting.ScriptObjectMirror
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.command.PluginCommand
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import org.bukkit.potion.PotionEffectType
import taboolib.common.platform.Platform
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.function.submit
import taboolib.common.platform.service.PlatformExecutor
import taboolib.library.reflex.Reflex.Companion.getProperty
import taboolib.library.reflex.Reflex.Companion.invokeMethod
import taboolib.module.nms.getI18nName
import taboolib.module.nms.getItemTag
import taboolib.platform.BukkitCommand
import taboolib.platform.util.hasName
import taboolib.platform.util.isNotAir
import java.util.concurrent.TimeUnit
import java.util.function.Consumer
import java.util.function.Function


object ScriptTool : BaseMap<String, Any>() {

    @ScriptTopLevel
    @JvmStatic
    fun task(task: PlatformExecutor.PlatformTask.() -> Unit) =
        submit { task(this) }

    @ScriptTopLevel
    @JvmStatic
    fun taskAsync(task: PlatformExecutor.PlatformTask.() -> Unit) =
        submit(async = true) { task(this) }

    @ScriptTopLevel
    @JvmStatic
    fun taskLater(task: PlatformExecutor.PlatformTask.() -> Unit, delay: Long) =
        submit(delay = delay) { task(this) }

    @ScriptTopLevel
    @JvmStatic
    fun taskAsyncLater(task: PlatformExecutor.PlatformTask.() -> Unit, delay: Long) =
        submit(delay = delay, async = true) { task(this) }

    @ScriptTopLevel
    @JvmStatic
    fun taskTimer(task: PlatformExecutor.PlatformTask.() -> Unit, delay: Long, period: Long) =
        submit(delay = delay, period = period) { task(this) }

    @ScriptTopLevel
    @JvmStatic
    fun taskAsyncTimer(task: PlatformExecutor.PlatformTask.() -> Unit, delay: Long, period: Long) =
        submit(async = true, delay = delay, period = period) { task(this) }

    @JvmStatic
    @Deprecated(
        "Old params; Please use 'task'",
        ReplaceWith("task(runnable)")
    )
    fun runTask(runnable: Runnable) =
        submit { runnable.run() }

    @JvmStatic
    @Deprecated(
        "Old params; Please ues 'taskAsync'",
        ReplaceWith("taskAsync(runnable)")
    )
    fun runTaskAsync(runnable: Runnable) =
        submit(async = true) { runnable.run() }

    @JvmStatic
    @Deprecated(
        "Old params; Please ues 'taskLater'",
        ReplaceWith("taskLater(runnable)")
    )
    fun runTaskLater(runnable: Runnable, delay: Long) =
        submit(delay = delay) { runnable.run() }

    @JvmStatic
    @Deprecated(
        "Old params; Please ues 'taskAsyncLater'",
        ReplaceWith("taskAsyncLater(runnable)")
    )
    fun runTaskAsyncLater(runnable: Runnable, delay: Long) =
        submit(delay = delay, async = true) { runnable.run() }

    @JvmStatic
    @Deprecated(
        "Old params; Please ues 'taskTimer'",
        ReplaceWith("taskTimer(runnable)")
    )
    fun runTaskTimer(runnable: Runnable, delay: Long, period: Long) =
        submit(delay = delay, period = period) { runnable.run() }

    @JvmStatic
    @Deprecated(
        "Old params; Please ues 'taskAsyncTimer'",
        ReplaceWith("taskAsyncTimer(runnable)")
    )
    fun runTaskAsyncTimer(runnable: Runnable, delay: Long, period: Long) =
        submit(async = true, delay = delay, period = period) { runnable.run() }


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
                return scriptManager.invoke<String>(path, arguments = arrayOf(player, params)).toString()
            }
        }.register()
    }

    @JvmStatic
    fun addListener(
        key: String,
        path: String,
        eventPriority: String = "NORMAL",
        ignoreCancel: Boolean = false,
        exec: Consumer<Any>
    ) {
        val clazz = path.findClass() ?: return
        val level: Int = try {
            EventPriority.valueOf(eventPriority.uppercase()).level
        } catch (e: Exception) {
            eventPriority.toIntOrNull() ?: 0
        }
        val platform: Platform = Platform.BUKKIT
        addListener(key, platform, clazz, level, ignoreCancel, exec)
    }

    @JvmStatic
    fun addListener(
        key: String,
        platformStr: String,
        path: String,
        eventPriority: String = "NORMAL",
        ignoreCancel: Boolean = false,
        exec: Consumer<Any>
    ) {
        val clazz = path.findClass() ?: return
        val level: Int = try {
            EventPriority.valueOf(eventPriority.uppercase()).level
        } catch (e: Exception) {
            eventPriority.toIntOrNull() ?: 0
        }
        val platform: Platform = try {
            Platform.valueOf(platformStr.uppercase())
        } catch (e: Exception) {
            Platform.BUKKIT
        }
        addListener(key, platform, clazz, level, ignoreCancel, exec)
    }

    @JvmStatic
    fun addListener(
        key: String,
        platform: Platform,
        event: Class<*>,
        level: Int = 0,
        ignoreCancel: Boolean = false,
        exec: Consumer<Any>
    ) {
        ScriptListener.Builder(key, platform, event, Priority(level), ignoreCancel) {
            exec.accept(it)
        }.build().register()
    }

    @JvmStatic
    fun removeListener(key: String) {
        listenerManager.remove(key)
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
    fun command(
        name: String
    ): PluginCommand {
        val bc = BukkitCommand()
        bc.sync()
        return bc.constructor.newInstance(name, Pouvoir.plugin)
    }

    @JvmStatic
    fun regCommand(command: PluginCommand) {
        val bc = BukkitCommand()
        bc.sync()
        bc.commandMap.register(command.name, command)
    }

    @JvmStatic
    fun unRegCommand(name: String) {
        val bc = BukkitCommand()
        bc.sync()
        bc.unregisterCommand(name)
    }

    @JvmStatic
    fun runTaskAsyncPool(task: Runnable) {
        Pouvoir.poolExecutor.execute { task.run() }
    }

    @JvmStatic
    fun runTaskAsyncLaterPool(task: Runnable, delay: Long) {
        Pouvoir.poolExecutor.schedule({ task.run() }, delay, TimeUnit.MILLISECONDS)
    }

    @JvmStatic
    fun runTaskAsyncTimerPool(task: Runnable, delay: Long, period: Long) {
        Pouvoir.poolExecutor.scheduleAtFixedRate({ task.run() }, delay, period, TimeUnit.MILLISECONDS)
    }

    @JvmStatic
    fun getItemName(itemStack: ItemStack, player: Player? = null): String? {
        return if (itemStack.isNotAir() && itemStack.hasName())
            itemStack.itemMeta?.displayName
        else
            itemStack.getI18nName(player)
    }

    @JvmStatic
    fun getItemName(itemStack: ItemStack): String? {
        return getItemName(itemStack, null)
    }

    @JvmStatic
    fun getEnchantName(enchantment: Enchantment, player: Player? = null): String {
        return enchantment.getI18nName(player)
    }

    @JvmStatic
    fun getEnchantName(enchantment: Enchantment): String {
        return getEnchantName(enchantment, null)
    }

    @JvmStatic
    fun getPotionEffectName(potionEffectType: PotionEffectType, player: Player? = null): String {
        return potionEffectType.getI18nName(player)
    }

    @JvmStatic
    fun getPotionEffectName(potionEffectType: PotionEffectType): String {
        return getPotionEffectName(potionEffectType, null)
    }

    @JvmStatic
    fun buildInventoryHolder(func: Function<Unit, Inventory>): InventoryHolder {
        return InventoryHolder { func.apply(Unit) }
    }

    @JvmStatic
    fun itemNBTMap(itemStack: ItemStack, strList: List<String> = emptyList()): MutableMap<String, Any> {
        val itemTag = itemStack.getItemTag()
        return itemTag.toMutableMap(strList)
    }

    @JvmStatic
    fun itemNBTMap(itemStack: ItemStack): MutableMap<String, Any> {
        return itemNBTMap(itemStack, emptyList())
    }

    @JvmStatic
    fun get(user: String, key: String): String? {
        return containerManager[user, key]
    }

    @JvmStatic
    fun delete(user: String, key: String) {
        containerManager.delete(user, key)
    }

    @JvmStatic
    fun set(user: String, key: String, value: String) {
        containerManager[user, key] = value
    }

    @JvmStatic
    fun get(player: Player, key: String): String? {
        return get(player.name, key)
    }

    @JvmStatic
    fun delete(player: Player, key: String) {
        delete(player.name, key)
    }

    @JvmStatic
    fun set(player: Player, key: String, value: String) {
        set(player.name, key, value)
    }

    @ScriptTopLevel
    @JvmStatic
    fun arrayOf(it: Any?): Array<Any?> {
        if (it is Array<*>) {
            return it as Array<Any?>
        }
        return if (it is ScriptObjectMirror) {
            if (it.isArray) it.toObject() as Array<Any?>
            else arrayOf(it.toObject())
        } else {
            kotlin.arrayOf(it)
        }
    }

    private fun ScriptObjectMirror.toObject(): Any? {
        if (this.isFunction) {
            val paramSize =
                (this.getProperty<Any>("sobj")!!).getProperty<Any>("data")!!.invokeMethod<Any>("getGenericType")
                    ?.invokeMethod<Int>("parameterCount")!! - 2
            return when (paramSize) {
                0 -> Runnable { this.call(null) }
                1 -> fun(param: Any?) {
                    this.call(
                        null,
                        param
                    )
                }
                else -> fun(params: Array<Any?>) { this.call(null, *params) }
            }
        }
        if (isEmpty()) return this
        if (isArray) {
            val list: MutableList<Any?> = ArrayList()
            for ((_, result) in this) {
                if (result is ScriptObjectMirror) {
                    list.add(result.toObject())
                } else {
                    list.add(result)
                }
            }
            return list.toTypedArray()
        }
        val map: MutableMap<String, Any?> = HashMap()
        for ((key, result) in this) {
            if (result is ScriptObjectMirror) {
                map[key] = result.toObject()
            } else {
                map[key] = result
            }
        }
        return map
    }

    @ScriptTopLevel
    @JvmStatic
    fun listOf(it: Any?): List<Any?> {
        if (it is List<*>) {
            return it
        }
        if (it is Array<*>) {
            return it.toList()
        }
        return if (it is ScriptObjectMirror) {
            if (it.isArray) it.values.toList()
            else listOf(it.toObject())
        } else {
            kotlin.collections.listOf(it)
        }
    }

}