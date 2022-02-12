package com.skillw.pouvoir.api.script

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.Pouvoir.listenerManager
import com.skillw.pouvoir.Pouvoir.scriptManager
import com.skillw.pouvoir.api.listener.ScriptListener
import com.skillw.pouvoir.api.map.BaseMap
import com.skillw.pouvoir.util.ClassUtils
import com.skillw.pouvoir.util.ItemUtils.toMutableMap
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
import org.bukkit.scheduler.BukkitRunnable
import taboolib.common.platform.event.EventPriority
import taboolib.expansion.DataContainer
import taboolib.expansion.getDataContainer
import taboolib.module.nms.getI18nName
import taboolib.module.nms.getItemTag
import taboolib.platform.BukkitCommand
import taboolib.platform.util.hasName
import taboolib.platform.util.isNotAir
import java.util.concurrent.TimeUnit
import java.util.function.Consumer
import java.util.function.Function

object ScriptTool : BaseMap<String, Any>() {
    @JvmStatic
    fun runTask(task: Consumer<Unit>) {
        object : BukkitRunnable() {
            override fun run() {
                task.accept(Unit)
            }
        }.runTask(Pouvoir.plugin)
    }

    @JvmStatic
    fun runTaskAsync(task: Consumer<Unit>) {
        object : BukkitRunnable() {
            override fun run() {
                task.accept(Unit)
            }
        }.runTaskAsynchronously(Pouvoir.plugin)
    }

    @JvmStatic
    fun runTaskLater(task: Consumer<Unit>, delay: Long) {
        object : BukkitRunnable() {
            override fun run() {
                task.accept(Unit)
            }
        }.runTaskLater(Pouvoir.plugin, delay)
    }

    @JvmStatic
    fun runTaskAsyncLater(task: Consumer<Unit>, delay: Long) {

        object : BukkitRunnable() {
            override fun run() {
                task.accept(Unit)
            }
        }.runTaskLaterAsynchronously(Pouvoir.plugin, delay)
    }

    @JvmStatic
    fun runTaskTimer(task: Consumer<Unit>, delay: Long, period: Long) {
        object : BukkitRunnable() {
            override fun run() {
                task.accept(Unit)
            }
        }.runTaskTimer(Pouvoir.plugin, delay, period)
    }

    @JvmStatic
    fun runTaskAsyncTimer(task: Consumer<Unit>, delay: Long, period: Long) {
        object : BukkitRunnable() {
            override fun run() {
                task.accept(Unit)
            }
        }.runTaskTimerAsynchronously(Pouvoir.plugin, delay, period)
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
                return scriptManager.invokePathWithFunction(path, args = arrayOf(player, params)).toString()
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
        val clazz = ClassUtils.getClass(path) ?: return
        val priority = try {
            EventPriority.valueOf(eventPriority.uppercase())
        } catch (e: Exception) {
            EventPriority.NORMAL
        }
        addListener(key, clazz, priority, ignoreCancel, exec)
    }

    @JvmStatic
    fun addListener(
        key: String,
        event: Class<*>,
        eventPriority: EventPriority = EventPriority.NORMAL,
        ignoreCancel: Boolean = false,
        exec: Consumer<Any>
    ) {
        ScriptListener.build(key, event, eventPriority, ignoreCancel) {
            exec.accept(it)
        }.register()
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
        return BukkitCommand().constructor.newInstance(name, Pouvoir.plugin)
    }

    @JvmStatic
    fun regCommand(command: PluginCommand) {
        BukkitCommand().commandMap.register(command.name, command)
    }

    @JvmStatic
    fun unRegCommand(name: String) {
        BukkitCommand().unregisterCommand(name)
    }

    @JvmStatic
    fun runTaskAsyncPool(task: Consumer<Unit>) {
        Pouvoir.poolExecutor.execute { task.accept(Unit) }
    }

    @JvmStatic
    fun runTaskAsyncLaterPool(task: Consumer<Unit>, delay: Long) {
        Pouvoir.poolExecutor.schedule({ task.accept(Unit) }, delay, TimeUnit.MILLISECONDS)
    }

    @JvmStatic
    fun runTaskAsyncTimerPool(task: Consumer<Unit>, delay: Long, period: Long) {
        Pouvoir.poolExecutor.scheduleAtFixedRate({ task.accept(Unit) }, delay, period, TimeUnit.MILLISECONDS)
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
    fun getContainer(player: Player): DataContainer {
        return player.getDataContainer()
    }

}