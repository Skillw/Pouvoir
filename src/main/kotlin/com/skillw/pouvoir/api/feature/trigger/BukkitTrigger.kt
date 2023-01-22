package com.skillw.pouvoir.api.feature.trigger

/**
 * Pou trigger
 *
 * 主要是将Bukkit事件名字格式化
 *
 * 如果Bukkit事件类上没有此注解或没有指定name
 *
 * 则其事件名称将会像这样格式化为触发器id：
 *
 * "PlayerMoveEvent" -> "player move"
 *
 * @constructor Create empty Pou trigger
 * @property name
 */
annotation class BukkitTrigger(val name: String = "")
