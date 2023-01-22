package com.skillw.pouvoir.api.manager.sub.script

import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.plugin.ManagerTime
import com.skillw.pouvoir.api.plugin.map.KeyMap
import com.skillw.pouvoir.api.script.PouFileCompiledScript
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.function.console
import java.io.File

/**
 * 脚本管理器
 *
 * 主要负责：
 * - 维护可编译的脚本文件
 * - 维护已编译脚本
 * - 执行脚本周期任务
 *
 * @constructor Create empty Script manager
 */
abstract class ScriptManager : Manager, KeyMap<String, PouFileCompiledScript>() {
    /**
     * 添加脚本文件
     *
     * @param file 单个脚本文件
     */
    abstract fun addScript(file: File)

    /**
     * 添加脚本文件夹
     *
     * @param file 脚本文件夹
     */
    abstract fun addScriptDir(file: File)

    /**
     * 调用脚本中的函数
     *
     * @param pathWithFunction 路径::函数名
     * @param arguments 参数
     * @param sender 接收结果的对象
     * @param parameters 函数参数
     * @param T 返回类型
     * @return 返回值
     */
    abstract fun <T> invoke(
        pathWithFunction: String,
        arguments: Map<String, Any> = emptyMap(),
        sender: ProxyCommandSender = console(),
        vararg parameters: Any?,
    ): T?

    /**
     * 调用脚本中的函数
     *
     * @param path 路径
     * @param function 函数名
     * @param arguments 参数
     * @param sender 接收结果的对象
     * @param parameters 函数参数
     * @param T 返回类型
     * @return 返回值
     */
    abstract fun <T> invoke(
        path: String,
        function: String = "main",
        arguments: Map<String, Any> = emptyMap(),
        sender: ProxyCommandSender = console(),
        vararg parameters: Any?,
    ): T?

    /**
     * 调用脚本中的函数
     *
     * @param script 预编译脚本
     * @param function 函数名
     * @param arguments 参数
     * @param sender 接收结果的对象
     * @param parameters 函数参数
     * @param T 返回类型
     * @return 返回值
     */
    abstract fun <T> invoke(
        script: PouFileCompiledScript,
        function: String = "main",
        arguments: Map<String, Any> = emptyMap(),
        sender: ProxyCommandSender = console(),
        vararg parameters: Any?,
    ): T?

    /**
     * 执行JS
     *
     * @param script js脚本内容
     * @param arguments 参数
     * @param sender
     * @param T 返回类型
     * @return 返回值
     */
    abstract fun <T> evalJs(
        script: String,
        arguments: Map<String, Any> = emptyMap(),
        sender: ProxyCommandSender = console(),
    ): T?


    /**
     * 搜索
     *
     * @param path 路径
     * @param sender 接收报错的对象
     * @param silent 如有错是否报
     * @return 查找到的预编译脚本
     */
    abstract fun search(
        path: String,
        sender: ProxyCommandSender = console(),
        silent: Boolean = true,
    ): PouFileCompiledScript?

    /**
     * 添加管理器周期任务
     *
     * @param managerTime 周期
     * @param key 任务id
     * @param exec 任务内容
     * @receiver
     */
    abstract fun addExec(managerTime: ManagerTime, key: String, exec: () -> Unit)

    /**
     * 删除管理器周期任务
     *
     * @param managerTime 周期
     * @param key 任务id
     */
    abstract fun removeExec(managerTime: ManagerTime, key: String)
}