package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.context.Context
import com.skillw.pouvoir.api.function.reader.IReader
import com.skillw.pouvoir.api.manager.sub.PouFunctionManager
import com.skillw.pouvoir.api.map.BaseMap
import com.skillw.pouvoir.internal.function.context.SimpleContext
import com.skillw.pouvoir.internal.function.reader.SimpleReader

object PouFunctionManagerImpl : PouFunctionManager() {
    override val key = "PouFunctionManager"
    override val priority = 4
    override val subPouvoir = Pouvoir

    override val namespaces = BaseMap<String, HashSet<PouFunction<*>>>()

    override fun register(value: PouFunction<*>) {
        val namespace = value.namespace
        if (namespaces.containsKey(namespace)) namespaces[namespace]?.add(value)
        else namespaces.put(value.namespace, HashSet<PouFunction<*>>().apply { add(value) })
        register(value.key, value)
    }


    override fun parse(text: String): String {
        return parse(text, receiver = {})?.toString() ?: text
    }

    override fun parse(string: String, namespaces: Array<String>, receiver: Context.() -> Unit): Any? {
        val reader = SimpleReader(string)
        return parse(reader, namespaces, receiver)
    }

    override fun parse(reader: IReader, namespaces: Array<String>, receiver: Context.() -> Unit): Any? {
        val context = SimpleContext().also(receiver)
        return parse(reader, namespaces, context)
    }

    override fun parse(reader: IReader, namespaces: Array<String>, context: Context): Any? {
        context.apply {
            namespaces.forEach {
                this@PouFunctionManagerImpl.namespaces[it]?.forEach { function ->
                    put(function.key, function)
                }
            }
        }
        with(reader) {
            while (hasNext()) {
                parseAny(context).also {
                    if (!hasNext()) return it
                }
            }
        }
        return null
    }

}