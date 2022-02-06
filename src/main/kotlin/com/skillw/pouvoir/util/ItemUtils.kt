package com.skillw.pouvoir.util

import com.skillw.pouvoir.Pouvoir
import io.lumine.xikage.mythicmobs.MythicMobs
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import taboolib.module.nms.ItemTag
import taboolib.module.nms.ItemTagData
import taboolib.module.nms.ItemTagList
import taboolib.module.nms.ItemTagType

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
            meta.setDisplayName(Pouvoir.pouPlaceHolderAPI.replace(player, meta.displayName));
        }
        if (meta.hasLore()) {
            val lores = ArrayList<String>()
            for (lore in meta.lore!!) {
                lores.add(Pouvoir.pouPlaceHolderAPI.replace(player, lore))
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

    @Suppress("IMPLICIT_CAST_TO_ANY")
    @JvmStatic
    fun ItemTagData.obj(): Any {
        val value = when (this.type) {
            ItemTagType.BYTE -> this.asByte()
            ItemTagType.SHORT -> this.asShort()
            ItemTagType.INT -> this.asInt()
            ItemTagType.LONG -> this.asLong()
            ItemTagType.FLOAT -> this.asFloat()
            ItemTagType.DOUBLE -> this.asDouble()
            ItemTagType.STRING -> this.asString()
            ItemTagType.BYTE_ARRAY -> this.asByteArray()
            ItemTagType.INT_ARRAY -> this.asIntArray()
            ItemTagType.COMPOUND -> this.asCompound()
            ItemTagType.LIST -> this.asList()
            else -> this.asString()
        }
        return when (value) {
            is ItemTag -> {
                value.toMutableMap()
            }
            is ItemTagList -> {
                val list = java.util.ArrayList<Any>()
                value.forEach {
                    list.add(it.obj())
                }
                list
            }
            else -> {
                value
            }
        }
    }

    @JvmStatic
    fun ItemTag.toMutableMap(strList: List<String> = emptyList()): MutableMap<String, Any> {
        val map = HashMap<String, Any>()
        for (it in this) {
            val key = it.key
            if (strList.contains(key)) continue
            val value = it.value.obj()
            map[key] = value
        }
        return map
    }
}