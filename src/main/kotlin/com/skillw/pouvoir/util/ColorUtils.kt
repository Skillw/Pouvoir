package com.skillw.pouvoir.util

import taboolib.module.chat.colored

/**
 * ClassName : com.skillw.classsystem.util.ColorUtils
 * Created by Glom_ on 2021-03-25 20:26:01
 * Copyright  2021 user. All rights reserved.
 */
object ColorUtils {
    @JvmStatic
    fun color(msg: String): String {
        return msg.colored()
    }

    @JvmStatic
    fun color(msgs: List<String>): List<String> {
        return msgs.colored()
    }

    @JvmStatic
    fun unColor(msg: String): String {
        return msg.colored()
    }

    @JvmStatic
    fun unColor(msgs: List<String>): List<String> {
        return msgs.colored()
    }
}