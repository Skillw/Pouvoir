package com.skillw.pouvoir.api.plugin.handler

import org.bukkit.plugin.Plugin

/**
 * @className ClassHandler
 *
 * @author Glom
 * @date 2022/7/18 12:20 Copyright 2022 user. All rights reserved.
 */
abstract class ClassHandler(val priority: Int) : Comparable<ClassHandler> {
    /**
     * Inject
     *
     * @param clazz
     * @param plugin
     */
    abstract fun inject(clazz: Class<*>, plugin: Plugin)

    override fun compareTo(other: ClassHandler): Int = if (this.priority == other.priority) 0
    else if (this.priority > other.priority) 1
    else -1
}