package com.skillw.pouvoir.api.script

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.Pouvoir.containerManager
import com.skillw.pouvoir.Pouvoir.listenerManager
import com.skillw.pouvoir.Pouvoir.scriptManager
import com.skillw.pouvoir.api.annotation.ScriptTopLevel
import com.skillw.pouvoir.api.listener.Priority
import com.skillw.pouvoir.api.listener.ScriptListener
import com.skillw.pouvoir.api.map.BaseMap
import com.skillw.pouvoir.api.placeholder.PouPlaceHolder
import com.skillw.pouvoir.internal.script.javascript.PouJavaScriptEngine
import com.skillw.pouvoir.util.ClassUtils
import com.skillw.pouvoir.util.ClassUtils.findClass
import com.skillw.pouvoir.util.ItemUtils.toMutableMap
import jdk.nashorn.api.scripting.ScriptObjectMirror
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.command.PluginCommand
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import org.bukkit.potion.PotionEffectType
import taboolib.common.platform.Platform
import taboolib.common.platform.ProxyParticle
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.function.adaptLocation
import taboolib.common.platform.function.adaptPlayer
import taboolib.common.platform.function.console
import taboolib.common.platform.function.submit
import taboolib.common.platform.sendTo
import taboolib.common.platform.service.PlatformExecutor
import taboolib.common.reflect.Reflex.Companion.invokeMethod
import taboolib.common.util.Vector
import taboolib.common5.Mirror
import taboolib.common5.mirrorNow
import taboolib.module.chat.TellrawJson
import taboolib.module.nms.getI18nName
import taboolib.module.nms.getItemTag
import taboolib.platform.BukkitCommand
import taboolib.platform.util.hasName
import taboolib.platform.util.hoverItem
import taboolib.platform.util.isNotAir
import java.util.function.Consumer
import java.util.function.Function


object ScriptTool : BaseMap<String, Any>() {

    @ScriptTopLevel
    @JvmStatic
    fun sync(task: () -> Any?) = taboolib.common.util.sync {
        task.invoke()
    }

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
    fun taskLater(delay: Long, task: PlatformExecutor.PlatformTask.() -> Unit) =
        submit(delay = delay) { task(this) }

    @ScriptTopLevel
    @JvmStatic
    fun taskAsyncLater(delay: Long, task: PlatformExecutor.PlatformTask.() -> Unit) =
        submit(delay = delay, async = true) { task(this) }

    @ScriptTopLevel
    @JvmStatic
    fun taskTimer(delay: Long, period: Long, task: PlatformExecutor.PlatformTask.() -> Unit) =
        submit(delay = delay, period = period) { task(this) }

    @ScriptTopLevel
    @JvmStatic
    fun taskAsyncTimer(delay: Long, period: Long, task: PlatformExecutor.PlatformTask.() -> Unit) =
        submit(async = true, delay = delay, period = period) { task(this) }

    @JvmStatic
    @Deprecated(
        "To-update params; Please use 'task'",
        ReplaceWith("task(runnable)")
    )
    fun runTask(runnable: Runnable) =
        submit { runnable.run() }

    @JvmStatic
    @Deprecated(
        "To-update params; Please ues 'taskAsync'",
        ReplaceWith("taskAsync(runnable)")
    )
    fun runTaskAsync(runnable: Runnable) =
        submit(async = true) { runnable.run() }

    @JvmStatic
    @Deprecated(
        "To-update params; Please ues 'taskLater'",
        ReplaceWith("taskLater(runnable)")
    )
    fun runTaskLater(runnable: Runnable, delay: Long) =
        submit(delay = delay) { runnable.run() }

    @JvmStatic
    @Deprecated(
        "To-update params; Please ues 'taskAsyncLater'",
        ReplaceWith("taskAsyncLater(runnable)")
    )
    fun runTaskAsyncLater(runnable: Runnable, delay: Long) =
        submit(delay = delay, async = true) { runnable.run() }

    @JvmStatic
    @Deprecated(
        "To-update params; Please ues 'taskTimer'",
        ReplaceWith("taskTimer(runnable)")
    )
    fun runTaskTimer(runnable: Runnable, delay: Long, period: Long) =
        submit(delay = delay, period = period) { runnable.run() }

    @JvmStatic
    @Deprecated(
        "To-update params; Please ues 'taskAsyncTimer'",
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
                return scriptManager.invoke<String>(path, parameters = arrayOf(player, params)).toString()
            }
        }.register()
    }


    @JvmStatic
    fun pouPlaceHolder(identifier: String, name: String, author: String, version: String, path: String) {
        object : PouPlaceHolder(identifier, name, author, version) {
            override fun onPlaceHolderRequest(params: String, entity: LivingEntity, def: String): String {
                return scriptManager.invoke<String>(path, parameters = arrayOf(entity, params)).toString()
            }
        }.register()
    }

    @JvmStatic
    fun addListener(
        key: String,
        path: String,
        eventPriority: String = "NORMAL",
        ignoreCancel: Boolean = false,
        exec: Consumer<Any>,
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
        exec: Consumer<Any>,
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
        exec: Consumer<Any>,
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
        name: String,
    ): PluginCommand {
        val bc = BukkitCommand()
        bc.sync()
        return bc.constructor.newInstance(name, Pouvoir.plugin)
    }

    @JvmStatic
    fun regCommand(command: PluginCommand) {
        task {
            val bc = BukkitCommand()
            bc.sync()
            bc.commandMap.register(command.name, command)
        }
    }

    @JvmStatic
    fun unRegCommand(name: String) {
        task {
            val bc = BukkitCommand()
            bc.sync()
            bc.unregisterCommand(name)
        }
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
        return if (it?.javaClass?.simpleName == "ScriptObjectMirror") {
            if (it.invokeMethod<Boolean>("isArray") == true) it.toObject() as Array<Any?>
            else arrayOf(it.toObject())
        } else {
            kotlin.arrayOf(it)
        }
    }

    internal fun Any.toObject(): Any = PouJavaScriptEngine.bridge.toObject(this)!!

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

    @JvmStatic
    fun sendParticle(
        particle: ProxyParticle,
        location: org.bukkit.Location,
        range: Double = 128.0,
        offset: Vector = Vector(0, 0, 0),
        count: Int = 1,
        speed: Double = 0.0,
        data: ProxyParticle.Data? = null,
    ) {
        particle.sendTo(adaptLocation(location), range, offset, count, speed, data)
    }

    @JvmStatic
    fun sendParticle(
        particle: ProxyParticle,
        location: org.bukkit.Location,
        range: Double = 128.0,
        count: Int = 1,
        speed: Double = 0.0,
    ) {
        sendParticle(particle, location, range, Vector(0, 0, 0), count, speed)
    }

    @JvmStatic
    fun hoverItem(tellrawJson: TellrawJson, itemStack: ItemStack): TellrawJson {
        return tellrawJson.hoverItem(itemStack)
    }

    @JvmStatic
    fun sendTellraw(tellrawJson: TellrawJson, player: Player) {
        tellrawJson.sendTo(adaptPlayer(player))
    }

    @ScriptTopLevel
    @JvmStatic
    fun monitorNow(key: String, func: () -> Any?): Any? {
        return mirrorNow(key) { func.invoke() }
    }

    @ScriptTopLevel
    @JvmStatic
    fun monitorFuture(key: String, func: Mirror.MirrorFuture<Any?>.() -> Unit): Any {
        return taboolib.common5.mirrorFuture(key, func)
    }

    @JvmStatic
    fun checkMonitor(key: String) {
        val options = Mirror.MirrorSettings()
        val collect = Mirror.MirrorCollect(options, "/", "/")
        Mirror.mirrorData.keys().toList().filter {
            it.startsWith(key)
        }.forEach { mirrorKey ->
            var point = collect
            mirrorKey.split(":").forEach {
                point = point.sub.computeIfAbsent(it) { _ -> Mirror.MirrorCollect(options, mirrorKey, it) }
            }
        }
        collect.print(console(), collect.getTotal(), 0)
    }

    @JvmStatic
    fun clearMonitor(key: String?) {
        key ?: kotlin.run {
            Mirror.mirrorData.clear()
            return
        }
        Mirror.mirrorData.keys().toList().filter {
            it.startsWith(key)
        }.forEach { mirrorKey ->
            Mirror.mirrorData.remove(mirrorKey)
        }
    }

}