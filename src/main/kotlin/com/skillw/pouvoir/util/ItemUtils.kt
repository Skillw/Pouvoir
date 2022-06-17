package com.skillw.pouvoir.util

import com.skillw.pouvoir.Pouvoir
import io.lumine.xikage.mythicmobs.MythicMobs
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import taboolib.common5.Coerce
import taboolib.module.nms.ItemTag
import taboolib.module.nms.ItemTagData
import taboolib.module.nms.ItemTagList
import taboolib.module.nms.ItemTagType
import java.util.regex.Pattern

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
            meta.setDisplayName(Pouvoir.pouPlaceHolderAPI.replace(player, meta.displayName))
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

    private val pattern = Pattern.compile("\\((?<type>byte|short|int|long|float|double|char|boolean|string)\\) ")

    @JvmStatic
    fun Any.toNBT(): ItemTagData? {
        return if (this is ItemTagData) {
            this
        } else if (this is String) {
            val matcher = pattern.matcher(this)
            return if (matcher.find()) {
                val type = matcher.group("type")
                val new = this.replace(matcher.group(0), "")
                when (type) {
                    "byte" -> Coerce.toByte(new).toNBT()
                    "short" -> Coerce.toShort(new).toNBT()
                    "int" -> Coerce.toInteger(new).toNBT()
                    "long" -> Coerce.toLong(new).toNBT()
                    "float" -> Coerce.toFloat(new).toNBT()
                    "double" -> Coerce.toDouble(new).toNBT()
                    "char" -> Coerce.toChar(new).toNBT()
                    "boolean" -> Coerce.toBoolean(new).toNBT()
                    else -> ItemTagData(new)
                }
            } else {
                ItemTagData(this)
            }
        } else if (this is Int) {
            ItemTagData(this)
        } else if (this is Double) {
            ItemTagData(this)
        } else if (this is Float) {
            ItemTagData(this)
        } else if (this is Short) {
            ItemTagData(this)
        } else if (this is Long) {
            ItemTagData(this)
        } else if (this is Byte) {
            ItemTagData(this)
        } else if (this is ByteArray) {
            ItemTagData(this)
        } else if (this is IntArray) {
            ItemTagData(this)
        } else if (this is List<*>) {
            ItemTagData.translateList(ItemTagList(), this)
        } else {
            val itemTag: ItemTag
            if (this is Map<*, *>) {
                itemTag = ItemTag()
                for (it in this) {
                    itemTag[it.key.toString()] = (it.value ?: continue).toNBT()
                }
                itemTag
            } else if (this is ConfigurationSection) {
                itemTag = ItemTag()
                this.getValues(false).forEach { (key: String?, value: Any) ->
                    itemTag[key] = value.toNBT()
                }
                itemTag
            } else {
                ItemTagData("Not supported: $this")
            }
        }
    }
}