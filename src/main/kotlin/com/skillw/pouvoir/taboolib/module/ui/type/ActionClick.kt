package com.skillw.pouvoir.taboolib.module.ui.type

import org.bukkit.inventory.ItemStack
import taboolib.common.Isolated
import com.skillw.pouvoir.taboolib.module.ui.ClickEvent


class ActionClick : Action() {

    override fun getCursor(e: ClickEvent): ItemStack {
        return e.clicker.itemOnCursor
    }

    override fun setCursor(e: ClickEvent, item: ItemStack?) {
        e.clicker.setItemOnCursor(item)
    }

    override fun getCurrentSlot(e: ClickEvent): Int {
        return e.rawSlot
    }
}