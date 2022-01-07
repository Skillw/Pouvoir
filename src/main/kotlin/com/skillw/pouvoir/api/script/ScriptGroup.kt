package com.skillw.pouvoir.api.script

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.`object`.BaseObject
import com.skillw.pouvoir.api.able.Invokable
import com.skillw.pouvoir.api.map.LowerMap
import org.bukkit.configuration.serialization.ConfigurationSerializable

abstract class ScriptGroup<T : BaseObject>() : LowerMap<String>(), Invokable<T>, ConfigurationSerializable {
    constructor(vararg pairs: Pair<String, String>) : this() {
        this.map.putAll(pairs)
    }

    constructor(map: Map<String, String>) : this() {
        this.map.putAll(map)
    }


    override fun invoke(key: String, obj: T, argsMap: MutableMap<String, Any>, vararg args: Any): Any? {
        val path = map[key] ?: return null
        argsMap["obj"] = obj
        return Pouvoir.scriptManager.invokePathWithFunction(path, argsMap = argsMap, args = args)
    }

    override fun serialize(): MutableMap<String, Any> {
        val map = HashMap<String, Any>()
        this.map.forEach {
            map[it.key] = it.value
        }
        return map
    }

}