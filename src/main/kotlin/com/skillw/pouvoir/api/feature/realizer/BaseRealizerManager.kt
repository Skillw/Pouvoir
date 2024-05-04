package com.skillw.pouvoir.api.feature.realizer

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.feature.realizer.component.Awakeable
import com.skillw.pouvoir.api.feature.realizer.component.Realizable
import com.skillw.pouvoir.api.feature.realizer.component.Switchable
import com.skillw.pouvoir.api.feature.realizer.component.Sync
import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.plugin.SubPouvoir
import com.skillw.pouvoir.api.plugin.map.KeyMap
import com.skillw.pouvoir.api.plugin.map.MultiMap
import com.skillw.pouvoir.util.completeYaml
import com.skillw.pouvoir.util.isAlive
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import taboolib.common.platform.function.isPrimaryThread
import taboolib.common5.FileWatcher
import taboolib.module.configuration.util.asMap
import java.io.File
import java.util.*

/**
 * @className BaseRealizerManager
 *
 * @author Glom
 * @date 2023/8/11 20:17 Copyright 2024 Glom.
 */
open class BaseRealizerManager(final override val key: String, final override val subPouvoir: SubPouvoir) : Manager,
    KeyMap<String, BaseRealizer>(), Realizable {
    override val priority: Int
        get() = 0

    private val fileMap = MultiMap<File, BaseRealizer>()
    private val awakeables = ArrayList<Awakeable>()
    private val realizables = ArrayList<Realizable>()
    private val switchable = ArrayList<Switchable>()
    private val syncs = ArrayList<Sync>()

    override fun onLoad() {
        values.filterIsInstance<Realizable>().forEach(realizables::add)
        values.filterIsInstance<Sync>().forEach(syncs::add)
        values.filterIsInstance<Awakeable>().forEach(awakeables::add)
        values.filterIsInstance<Switchable>().forEach(switchable::add)
        onReload()
        awakeables.filter {
            it !is Switchable || it.isEnable()
        }.forEach(Awakeable::onLoad)
    }

    override fun onEnable() {
        awakeables.filter { it !is Switchable || it.isEnable() }.forEach(Awakeable::onEnable)
    }

    override fun onActive() {
        awakeables.filter { it !is Switchable || it.isEnable() }.forEach(Awakeable::onActive)
    }

    private val watcher = FileWatcher()


    override fun onReload() {
        fileMap.keys.forEach(this::reloadFile)
        awakeables.filter { it !is Switchable || it.isEnable() }.forEach(Awakeable::onReload)
    }

    private fun realizable(func: (Realizable) -> Unit) {
        realizables
            .filter { it !is Switchable || it.isEnable() }
            .forEach(func)
    }

    private val tasks = Collections.synchronizedList(ArrayList<() -> Unit>())

    private fun genSyncTasks(entity: LivingEntity) {
        syncs
            .filter { it !is Switchable || it.isEnable() }
            .forEach {
                it.newTask(entity)?.let { task ->
                    if (isPrimaryThread)
                        task.invoke()
                    else tasks += task
                }
            }
    }

    private fun executeTasks() {
        val iter = tasks.iterator()
        while (iter.hasNext()) {
            iter.next()()
            iter.remove()
        }
    }

    fun executeSyncTasks() {
        if (!subPouvoir.plugin.isEnabled) return
        if (isPrimaryThread) executeTasks()
        else taboolib.common.util.sync { executeTasks() }
    }


    override fun realize(entity: LivingEntity) {
        if (!entity.isAlive()) return
        if (entity is Player) Pouvoir.antiCheatManager.bypass(entity)
        realizable {
            it.realize(entity)
        }
        genSyncTasks(entity)
        if (entity is Player) Pouvoir.antiCheatManager.recover(entity)
    }

    override fun unrealize(entity: LivingEntity) {
        if (!entity.isAlive()) return
        realizable {
            it.unrealize(entity)
        }
    }

    override fun onDisable() {
        awakeables.filter { it !is Switchable || it.isEnable() }.forEach(Awakeable::onDisable)
    }

    private fun reloadFile(file: File) {
        val realizers = fileMap[file] ?: return
        val configs = subPouvoir.plugin.completeYaml(file, realizers.map { it.ignorePaths }.flatten().toSet())
        realizers.forEach { realizer ->
            val pre = realizer !is Switchable || realizer.isEnable()
            val config = realizer.config
            config.clear()
            configs[realizer.key]
                ?.asMap()
                ?.entries
                ?.associate { (key, value) -> key to value!! }
                ?.let(config::putAll)
            val post = realizer !is Switchable || realizer.isEnable()
            if (realizer is Switchable) {
                if (pre && !post)
                    realizer.whenDisable()
                else if (!pre && post)
                    realizer.whenEnable()
            }
        }
    }

    override fun put(key: String, value: BaseRealizer): BaseRealizer? {
        val file = value.file
//        if (!watcher.hasListener(file)) {
//            watcher.addSimpleListener(file) { reloadFile(file) }
//        }
        return super.put(key, value).also { fileMap += file to value }
    }

    override fun register() {
        subPouvoir.managerData.onReload("RealizerManager-$key") {
            onReload()
        }
    }

}