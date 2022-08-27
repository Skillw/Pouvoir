package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.context.IContext
import com.skillw.pouvoir.api.function.parser.Parser
import com.skillw.pouvoir.api.function.reader.IReader
import com.skillw.pouvoir.api.manager.sub.PouFunctionManager
import com.skillw.pouvoir.api.map.BaseMap
import com.skillw.pouvoir.internal.core.function.context.SimpleContext
import com.skillw.pouvoir.internal.core.function.reader.SimpleReader

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

    override fun remove(key: String): PouFunction<*>? {
        return super.remove(key)?.apply {
            aliases.forEach { remove(it) }
        }
    }


    private val cache = BaseMap<Int, SimpleReader>()
    override fun eval(string: String, namespaces: Array<String>, context: IContext): Any? {
        return parse(SimpleReader(cache.getOrPut(string.hashCode()) { SimpleReader(string) }), namespaces, context)
    }

    override fun eval(string: String, namespaces: Array<String>, receiver: IContext.() -> Unit): Any? {
        return eval(string, namespaces, SimpleContext().apply(receiver))
    }

    private fun parse(reader: IReader, namespaces: Array<String>, context: IContext): Any? {
        context.apply {
            namespaces.forEach {
                this@PouFunctionManagerImpl.namespaces[it]?.forEach { function ->
                    functions[function.key] = function
                }
            }
        }
        val parser = Parser.createParser(reader, context)
        with(parser) {
            while (hasNext()) {
                parseNext<Any?>()?.also {
                    if (!hasNext()) return it
                } ?: break
            }
        }
        return null
    }

}