package com.skillw.pouvoir.api.able

import org.bukkit.inventory.ItemStack

/**
 * ClassName : com.skillw.com.skillw.rpglib.api.able.Iconable
 * Created by Glom_ on 2021-04-03 23:50:47
 * Copyright  2021 user. All rights reserved.
 */
interface Iconable<T> : Keyable<T> {
    var icon: ItemStack
}