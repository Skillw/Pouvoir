package com.skillw.pouvoir.internal.script.common.top

import com.skillw.pouvoir.api.map.BaseMap
import com.skillw.pouvoir.api.map.KeyMap
import com.skillw.pouvoir.util.MapUtils.put
import taboolib.module.chat.colored
import java.util.*

internal object TopLevel : KeyMap<String, TopLevelData>() {
    private val membersInfo = BaseMap<String, MutableMap<Type, LinkedList<String>>>()

    private fun LinkedList<String>.addTypeMessages(type: Type, data: MutableMap<Type, LinkedList<String>>) {
        val messages = data[type]?.sorted()
        add("    &f- &a${type.display}&f: " + if (messages == null || messages.isEmpty()) "&7Empty" else "")
        data[type]?.sorted()?.forEach {
            add("       $it")
        }
    }

    @JvmStatic
    fun MutableMap<String, Any?>.putAllTopLevel() {
        TopLevel.values.forEach {
            this[it.key] = it.member
        }
    }

    fun getInfo(): LinkedList<String> {
        val list = LinkedList<String>()
        list.add("&5&lTop members&f:")
        membersInfo.keys.sorted().forEach { source ->
            val data = membersInfo[source] ?: return@forEach
            list.add("  &f-> &6From &d$source&f:")
            list.addTypeMessages(Type.CLASS, data)
            list.addTypeMessages(Type.FIELD, data)
            list.addTypeMessages(Type.FUNCTION, data)
        }
        return list
    }

    override fun register(key: String, value: TopLevelData) {
        membersInfo.put(value.source, value.type, value.info.colored())
        super.register(key, value)
    }

    enum class Type(val display: String) {
        CLASS("Classes"), FUNCTION("Functions"), FIELD("Fields")
    }
}