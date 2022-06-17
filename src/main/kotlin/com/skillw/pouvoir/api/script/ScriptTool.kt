package com.skillw.pouvoir.api.script

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.ListenerPriority
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import com.comphenix.protocol.events.PacketListener
import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.Pouvoir.listenerManager
import com.skillw.pouvoir.Pouvoir.playerDataManager
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
import taboolib.common5.Coerce
import taboolib.expansion.DataContainer
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
    fun runTask(task: Runnable) {
        object : BukkitRunnable() {
            override fun run() {
                task.run()
            }
        }.runTask(Pouvoir.plugin)
    }

    @JvmStatic
    fun runTaskAsync(task: Runnable) {
        object : BukkitRunnable() {
            override fun run() {
                task.run()
            }
        }.runTaskAsynchronously(Pouvoir.plugin)
    }

    @JvmStatic
    fun runTaskLater(task: Runnable, delay: Long) {
        object : BukkitRunnable() {
            override fun run() {
                task.run()
            }
        }.runTaskLater(Pouvoir.plugin, delay)
    }

    @JvmStatic
    fun runTaskAsyncLater(task: Runnable, delay: Long) {

        object : BukkitRunnable() {
            override fun run() {
                task.run()
            }
        }.runTaskLaterAsynchronously(Pouvoir.plugin, delay)
    }

    @JvmStatic
    fun runTaskTimer(task: Runnable, delay: Long, period: Long) {
        object : BukkitRunnable() {
            override fun run() {
                task.run()
            }
        }.runTaskTimer(Pouvoir.plugin, delay, period)
    }

    @JvmStatic
    fun runTaskAsyncTimer(task: Runnable, delay: Long, period: Long) {
        object : BukkitRunnable() {
            override fun run() {
                task.run()
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
    fun getContainer(player: Player): DataContainer? {
        println("Outdated function Tool#getContainer!")
        println("Please use Tool#get / Tool#set / Tool#delete !")
        return null
    }

    @JvmStatic
    fun get(user: String, key: String): String? {
        return playerDataManager[user, key]
    }

    @JvmStatic
    fun delete(user: String, key: String) {
        playerDataManager.delete(user, key)
    }

    @JvmStatic
    fun set(user: String, key: String, value: String) {
        playerDataManager[user, key] = value
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

    private val packetListeners by lazy {
        BaseMap<String, PacketListener>()
    }

    @JvmStatic
    fun addPacketListener(
        key: String,
        priority: ListenerPriority,
        types: Array<PacketType>,
        sending: Consumer<PacketEvent>,
        receiving: Consumer<PacketEvent>
    ) {
        val listener = object : PacketAdapter(Pouvoir.plugin, priority, *types) {
            override fun onPacketSending(event: PacketEvent) {
                sending.accept(event)
            }

            override fun onPacketReceiving(event: PacketEvent) {
                receiving.accept(event)
            }
        }
        packetListeners[key] = listener
        ProtocolLibrary.getProtocolManager().addPacketListener(listener)
    }

    @JvmStatic
    fun addPacketListener(
        key: String,
        priority: String,
        types: Array<PacketType>,
        sending: Consumer<PacketEvent>,
        receiving: Consumer<PacketEvent>
    ) {
        val listenerPriority = Coerce.toEnum(priority, ListenerPriority::class.java, ListenerPriority.NORMAL)!!
        addPacketListener(key, listenerPriority, types, sending, receiving)
    }

    @JvmStatic
    fun removePacketListener(
        key: String
    ) {
        ProtocolLibrary.getProtocolManager().removePacketListener(packetListeners[key] ?: return)
    }
}