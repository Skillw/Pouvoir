package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.function.PouFunction
import com.skillw.pouvoir.api.function.context.IContext
import com.skillw.pouvoir.api.manager.sub.function.PouFunctionManager
import com.skillw.pouvoir.api.map.BaseMap
import com.skillw.pouvoir.internal.core.function.eval.SimpleEvaluator

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

    override fun eval(string: String, namespaces: Array<String>, context: IContext): Any? {
        return SimpleEvaluator(namespaces, context).evaluate(string)
    }

    override fun eval(string: String, namespaces: Array<String>, receiver: IContext.() -> Unit): Any? {
        return SimpleEvaluator(namespaces, receiver).evaluate(string)
    }

}