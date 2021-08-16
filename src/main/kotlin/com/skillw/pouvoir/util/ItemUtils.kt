package com.skillw.pouvoir.util

import com.skillw.pouvoir.Pouvoir
import io.lumine.xikage.mythicmobs.MythicMobs
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

/**
 * ClassName : com.skillw.classsystem.util.ItemUtils
 * Created by Glom_ on 2021-03-12 23:40:01
 * Copyright  2021 user. All rights reserved.
 */
object ItemUtils {
    @JvmStatic
    fun papiItem(item: ItemStack, player: Player): ItemStack {
        if (!item.hasItemMeta()) {
            return item
        }
        val meta = item.itemMeta
        meta as ItemMeta
        if (meta.hasDisplayName()) {
            meta.setDisplayName(Pouvoir.rpgPlaceHolderAPI.replace(player, meta.displayName));
        }
        if (meta.hasLore()) {
            val lores = ArrayList<String>()
            for (lore in meta.lore!!) {
                lores.add(Pouvoir.rpgPlaceHolderAPI.replace(player, lore))
            }
            meta.lore = lores
        }
        item.itemMeta = meta
        return item
    }

    @JvmStatic
    fun getMythicItem(itemID: String, player: Player): ItemStack? {
        val item = MythicMobs.inst().itemManager.getItemStack(itemID)
        if (item != null) {
            return papiItem(item, player)
        }
        return null
    }

    @JvmStatic
    fun getMythicItem(itemID: String): ItemStack? {
        val item = MythicMobs.inst()?.itemManager?.getItemStack(itemID)
        if (item != null) {
            return item
        }
        return null
    }

    @JvmStatic
    fun getMythicItems(list: List<String>, player: Player?): ArrayList<ItemStack> {
        val itemStacks: ArrayList<ItemStack> = java.util.ArrayList()
        for (id in list) {
            val itemStack = if (player == null) getMythicItem(id) else getMythicItem(
                id, player
            )
            if (itemStack != null) itemStacks.add(itemStack)
        }
        return itemStacks
    }

}