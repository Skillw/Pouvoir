package com.skillw.asahi.internal.context

import com.skillw.asahi.api.member.context.AsahiContext
import com.skillw.asahi.api.member.context.AsahiContext.Companion.getters
import com.skillw.asahi.api.member.context.AsahiContext.Companion.setters
import com.skillw.asahi.api.member.quest.LazyQuester
import com.skillw.asahi.api.script.AsahiEngine
import com.skillw.asahi.api.script.linking.Invoker
import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.util.getDeep
import com.skillw.pouvoir.util.putDeep
import com.skillw.pouvoir.util.safe
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.LinkedBlockingDeque


/**
 * @className AsahiContextImpl
 *
 * @author Glom
 * @date 2022/12/24 15:05 Copyright 2022 user. All rights reserved.
 */
internal class AsahiContextImpl private constructor(
    private val data: MutableMap<String, Any> = HashMap(),
) : AsahiContext, MutableMap<String, Any> by data {

    private constructor(other: AsahiContext) : this(HashMap()) {
        putAll(other)
    }

    private val onExit: Deque<() -> Unit> = LinkedBlockingDeque()

    override val invokers = HashMap<String, Invoker>()
    private val tasks = CopyOnWriteArrayList<CompletableFuture<*>>()

    override val entries: MutableSet<MutableMap.MutableEntry<String, Any>>
        get() = data.entries
    override val keys: MutableSet<String>
        get() = data.keys
    override val values: MutableCollection<Any>
        get() = data.values
    override val size: Int
        get() = data.size

    override fun get(key: String): Any? {
        return (getters.firstOrNull { it.filterKey(this, key) } ?: return getOrigin(key)).get(this, key)
    }

    override fun put(key: String, value: Any): Any? {
        return (setters.firstOrNull { it.filterKey(this, key) } ?: return setOrigin(key, value)).set(this, key, value)
    }

    override fun getOrigin(key: String): Any? {
        return (if (key.contains(".")) getDeep(key) else data[key]).run {
            if (this is LazyQuester<*>) get() else this
        }
    }

    override fun setOrigin(key: String, value: Any): Any? {
        return if (key.contains(".")) data.putDeep(key, value) else data[key] = value
    }

    override fun putDeep(key: String, value: Any): Any? {
        return data.putDeep(key, value)
    }

    override fun getDeep(key: String): Any? {
        return data.getDeep(key)
    }


    override fun putAll(from: Map<out String, Any>) {
        if (from is AsahiContext) {
            invokers.putAll(from.invokers)
        }
        data.putAll(from)
    }

    override fun putAllIfExists(map: Map<String, Any>) {
        map.forEach { (key, value) ->
            if (containsKey(key)) put(key, value)
        }
    }

    override fun invoke(key: String, vararg params: Any?): Any? {
        val func = invokers[key] ?: error("No such function called $key")
        return func.invoke(this, *params)
    }

    override fun import(vararg paths: String) {
        paths.forEach {
            Pouvoir.scriptManager.search(it)?.let { script ->
                putAll((script.script.engine as AsahiEngine).context())
            }
        }
    }

    override fun <R : Any> select(obj: R): R {
        put("@selector", obj)
        return obj
    }


    override fun clone(): AsahiContext {
        return AsahiContextImpl(this)
    }

    override fun onExit(exec: () -> Unit) {
        onExit.addFirst(exec)
    }

    override fun <R> CompletableFuture<R>.autoCancelled(): CompletableFuture<R> {
        onExit { cancel(true) }
        return this
    }

    private var exit = false
    override fun isExit(): Boolean {
        return exit
    }

    override fun exit() {
        while (onExit.isNotEmpty()) {
            safe { onExit.pollFirst()() }
        }
        exit = true
    }

    private var debug = false
    override fun debugOn() {
        debug = true
    }

    override fun debugOff() {
        debug = false
    }

    override fun ifDebug(todo: () -> Unit) {
        if (debug) todo()
    }

    override fun <R> R.ifDebug(todo: (R) -> Unit): R {
        if (debug) todo(this)
        return this
    }

    override fun <R> temp(vararg pairs: Pair<String, Any?>, todo: () -> R): R {
        val origins = HashMap<String, Any>()
        pairs.forEach { (key, value) ->
            origins[key] = get(key) ?: Unit
            if (value != null)
                put(key, value)
            else
                remove(key)
        }
        val result = todo()
        pairs.forEach { (key, _) ->
            remove(key)
        }
        origins.forEach { (key, value) ->
            if (value == Unit) return@forEach
            put(key, value)
        }
        return result
    }

    override fun reset() {
        exit = false
    }

    override fun toString(): String {
        return "Variables: $data \n Functions: $invokers"
    }

    override fun addTask(task: CompletableFuture<*>) {
        tasks.add(task)
    }

    override fun awaitAllTask() {
        CompletableFuture.allOf(*tasks.toTypedArray()).join()
    }

    companion object {
        //给脚本用的 不用填参数
        fun create(): AsahiContextImpl {
            return AsahiContextImpl()
        }

        fun create(
            data: MutableMap<String, Any> = HashMap(),
        ): AsahiContextImpl {
            return AsahiContextImpl(data)
        }
    }
}