package com.skillw.pouvoir.api.manager.sub.script

import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.map.KeyMap
import com.skillw.pouvoir.internal.core.script.common.PouCompiledScript
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.function.console
import java.io.File

/**
 * Script manager
 *
 * @constructor Create empty Script manager
 */
abstract class ScriptManager : Manager, KeyMap<String, PouCompiledScript>() {
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
     * Invoke
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
     * Invoke
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
     * Invoke
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
        script: PouCompiledScript,
        function: String = "main",
        arguments: Map<String, Any> = emptyMap(),
        sender: ProxyCommandSender = console(),
        vararg parameters: Any?,
    ): T?

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
    ): PouCompiledScript?
}