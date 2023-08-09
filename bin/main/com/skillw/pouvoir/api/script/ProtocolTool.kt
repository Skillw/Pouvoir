package com.skillw.pouvoir.api.script

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.ListenerPriority
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import com.comphenix.protocol.events.PacketListener
import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.plugin.map.BaseMap
import taboolib.common5.Coerce
import java.util.function.Consumer

object ProtocolTool {
    private val packetListeners by lazy {
        BaseMap<String, PacketListener>()
    }


    fun addPacketListener(
        key: String,
        priority: ListenerPriority,
        types: Array<PacketType>,
        sending: Consumer<PacketEvent>,
        receiving: Consumer<PacketEvent>,
    ) {
        val listener = object : PacketAdapter(Pouvoir.plugin, priority, *types) {
            override fun onPacketSending(event: PacketEvent) {
                sending.accept(event)
            }

            override fun onPacketReceiving(event: PacketEvent) {
                receiving.accept(event)
            }
        }
        packetListeners[key] = listener
        ProtocolLibrary.getProtocolManager().addPacketListener(listener)
    }


    fun addPacketListener(
        key: String,
        priority: String,
        types: Array<PacketType>,
        sending: Consumer<PacketEvent>,
        receiving: Consumer<PacketEvent>,
    ) {
        val listenerPriority = Coerce.toEnum(priority, ListenerPriority::class.java, ListenerPriority.NORMAL)!!
        addPacketListener(key, listenerPriority, types, sending, receiving)
    }


    fun removePacketListener(
        key: String,
    ) {
        ProtocolLibrary.getProtocolManager().removePacketListener(packetListeners[key] ?: return)
    }
}