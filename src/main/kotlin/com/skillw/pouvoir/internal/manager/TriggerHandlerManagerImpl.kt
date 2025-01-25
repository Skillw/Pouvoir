package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.Pouvoir.triggerManager
import com.skillw.pouvoir.api.feature.handler.BaseDispatcher
import com.skillw.pouvoir.api.feature.handler.BaseHandler
import com.skillw.pouvoir.api.feature.trigger.BaseTrigger
import com.skillw.pouvoir.api.manager.sub.TriggerHandlerManager
import com.skillw.pouvoir.api.plugin.SubPouvoir
import com.skillw.pouvoir.api.plugin.map.BaseMap
import com.skillw.pouvoir.api.plugin.`object`.Releasable
import com.skillw.pouvoir.internal.feature.dispatcher.SimpleDispatcherBuilder
import com.skillw.pouvoir.internal.feature.handler.AsahiHandlerBuilder
import com.skillw.pouvoir.util.loadMultiply
import com.skillw.pouvoir.util.loadYaml
import com.skillw.pouvoir.util.put
import com.skillw.pouvoir.util.safe
import taboolib.common5.FileWatcher
import java.io.File

internal object TriggerHandlerManagerImpl : TriggerHandlerManager() {
    override val key = "TriggerHandlerManager"
    override val priority = 6
    override val subPouvoir = Pouvoir
    private val fileWatcher = FileWatcher(20)
    private val dataFolders = HashSet<File>()
    private val fileToKeys = BaseMap<File, HashSet<String>>()
    private val folderToKeys = BaseMap<File, HashSet<String>>()
    override fun unregister(key: String) {
        this[key]?.triggers?.forEach { trigger ->
            triggerManager.remove(trigger, key)
        }
    }

    override fun onEnable() {
        addSubPouvoir(Pouvoir)
    }

    override fun reloadFolder(folder: File) {
        dataFolders.add(folder)
        folderToKeys[folder]?.forEach(::unregister)
        loadMultiply(
            File(folder, "dispatchers"), SimpleDispatcherBuilder::class.java
        ).forEach {
            val (builder, file) = it
            safe { builder.build().register() }
            fileToKeys.put(file, builder.key)
            folderToKeys.put(folder, builder.key)
            fileWatcher.removeListener(file)
            fileWatcher.addSimpleListener(file) {
                reloadFile(file)
            }
        }
        loadMultiply(
            File(folder, "handlers"), AsahiHandlerBuilder::class.java
        ).forEach {
            val (builder, file) = it
            safe { builder.build().register() }
            fileToKeys.put(file, builder.key)
            folderToKeys.put(folder, builder.key)
            fileWatcher.removeListener(file)
            fileWatcher.addSimpleListener(file) {
                reloadFile(file)
            }
        }
    }

    private fun reloadFile(file: File) {
        fileToKeys[file]?.let {
            val dispatcher = get(it.first()) is BaseDispatcher
            it.forEach(::unregister)
            fileToKeys.remove(file)
            val yaml = file.loadYaml() ?: return
            if (dispatcher) {
                yaml.apply {
                    getKeys(false).forEach { key ->
                        SimpleDispatcherBuilder.deserialize(getConfigurationSection(key)!!).build().register()
                        fileToKeys.put(file, key)
                    }
                }
            } else {
                yaml.apply {
                    getKeys(false).forEach { key ->
                        AsahiHandlerBuilder.deserialize(getConfigurationSection(key)!!).build().register()
                        fileToKeys.put(file, key)
                    }
                }
            }
        }
    }

    override fun onReload() {
        values.filterIsInstance<Releasable>().forEach(Releasable::unregister)
        refreshFileListener {
            dataFolders.forEach(::reloadFolder)
        }
    }

    private fun refreshFileListener(todo: () -> Unit) {
        fileToKeys.keys.forEach(fileWatcher::removeListener)
        fileToKeys.clear()
        todo()
        fileToKeys.keys.forEach { file ->
            fileWatcher.addSimpleListener(file) {
                reloadFile(file)
            }
        }
    }

    override fun addDataFolders(folder: File) {
        dataFolders.add(folder)
        onReload()
    }

    override fun addSubPouvoir(subPouvoir: SubPouvoir) {
        val folder = subPouvoir.plugin.dataFolder
        addDataFolders(folder)
        subPouvoir.managerData.onReload {
            reloadFolder(folder)
        }
    }

    override fun put(key: String, value: BaseHandler<*>): BaseHandler<*>? {
        value.triggers.forEach { triggerKey ->
            value as BaseHandler<BaseTrigger>
            triggerManager.addTask<BaseTrigger>(triggerKey, key, value.priority) {
                value.handle(it)
            }
        }
        return super.put(key, value)
    }

}