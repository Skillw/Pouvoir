package com.skillw.pouvoir.api.script

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.map.LowerMap
import org.bukkit.configuration.serialization.ConfigurationSerializable

abstract class ScriptGroup() : LowerMap<String>(), ConfigurationSerializable {
    constructor(vararg pairs: Pair<String, String>) : this() {
        this.map.putAll(pairs)
    }

    constructor(map: Map<String, String>) : this() {
        this.map.putAll(map)
    }


    fun invoke(key: String, argsMap: MutableMap<String, Any>): Any? {
        val string = map[key] ?: return null
        return Pouvoir.scriptManager.invoke(string, argsMap = argsMap)
    }

    override fun serialize(): MutableMap<String, Any> {
        val map = HashMap<String, Any>()
        this.map.forEach {
            map[it.key] = it.value
        }
        return map
    }

}