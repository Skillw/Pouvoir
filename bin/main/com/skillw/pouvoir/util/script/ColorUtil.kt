package com.skillw.pouvoir.util.script

import taboolib.module.chat.colored
import taboolib.module.chat.uncolored

/**
 * 给脚本用的，颜色工具类
 *
 * Created by Glom_ on 2021-03-25 20:26:01
 *
 * Copyright 2021 user. All rights reserved.
 */
object ColorUtil {
    @JvmStatic
    fun colored(msg: String): String {
        return msg.colored()
    }

    @JvmStatic
    fun colored(messages: List<String>): List<String> {
        return messages.colored()
    }


    @JvmStatic
    fun uncolored(msg: String): String {
        return msg.uncolored()
    }


    @JvmStatic
    fun uncolored(messages: List<String>): List<String> {
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