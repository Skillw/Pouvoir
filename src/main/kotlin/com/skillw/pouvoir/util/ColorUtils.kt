package com.skillw.pouvoir.util

import taboolib.module.chat.colored
import taboolib.module.chat.uncolored

/**
 * 给我整吐了，怎么有人插件是什么功能都不看，上来劈头盖脸说我重复造轮子
 *
 * 在这里声明一下： 这是给脚本直接调用的，你脚本里能直接调到TLib提供的顶级函数？
 *
 * Created by Glom_ on 2021-03-25 20:26:01
 *
 * Copyright 2021 user. All rights reserved.
 */
object ColorUtils {

    @JvmStatic
    fun color(msg: String): String {
        return msg.colored()
    }

    @JvmStatic
    fun color(messages: List<String>): List<String> {
        return messages.colored()
    }

    @JvmStatic
    fun unColor(msg: String): String {
        return msg.uncolored()
    }

    @JvmStatic
    fun unColor(messages: List<String>): List<String> {
        return messages.uncolored()
    }

    @JvmStatic
    fun String.decolored(): String {
        return this.replace("§", "&")
    }

    @JvmStatic
    fun List<String>.decolored(): List<String> {
        return this.map { it.decolored() }
    }
}