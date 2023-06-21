package com.skillw.pouvoir.api.manager.sub

import com.skillw.pouvoir.api.feature.handler.BaseHandler
import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.plugin.SubPouvoir
import com.skillw.pouvoir.api.plugin.map.KeyMap
import java.io.File

/** TriggerManager 监听器处理器管理器，主要负责维护监听器处理器 */
abstract class TriggerHandlerManager : Manager, KeyMap<String, BaseHandler<*>>() {
    /**
     * 注销监听器处理器
     *
     * @param key 监听器处理器id
     */
    abstract fun unregister(key: String)
    abstract fun addDataFolders(folder: File)
    abstract fun addSubPouvoir(subPouvoir: SubPouvoir)
    abstract fun reloadFolder(folder: File)
}