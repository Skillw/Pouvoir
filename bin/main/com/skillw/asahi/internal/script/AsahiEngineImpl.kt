package com.skillw.asahi.internal.script

import com.skillw.asahi.api.member.context.AsahiContext
import com.skillw.asahi.api.member.lexer.AsahiLexer
import com.skillw.asahi.api.script.AsahiCompiledScript
import com.skillw.asahi.api.script.AsahiEngine
import com.skillw.asahi.api.script.AsahiEngineFactory
import com.skillw.asahi.internal.context.AsahiContextImpl
import java.io.File
import java.io.Reader
import java.util.concurrent.ConcurrentHashMap
import javax.script.Bindings
import javax.script.ScriptContext
import javax.script.ScriptEngineFactory

/**
 * @className AsahiEngine
 *
 * @author Glom
 * @date 2022/12/24 14:02 Copyright 2022 user. All rights reserved.
 */
class AsahiEngineImpl private constructor(
    private val factory: AsahiEngineFactory,
    context: AsahiContext = AsahiContext.create(),
) :
    AsahiEngine(context) {


    private val cache = ConcurrentHashMap<Int, AsahiCompiledScript>()

    override fun eval(script: String, context: ScriptContext): Any? {
        val other = context.getBindings(ScriptContext.ENGINE_SCOPE) as? AsahiContext ?: return null
        context().putAll(other)
        return compile(script).run(context())
    }

    override fun eval(script: String, bindings: Bindings): Any? {
        context().putAll(bindings)
        return compile(script).run(context())
    }

    override fun eval(reader: Reader, bindings: Bindings): Any? {
        return eval(reader.readText(), bindings)
    }

    override fun eval(reader: Reader, context: ScriptContext): Any? {
        return eval(reader.readText(), context)
    }

    override fun eval(script: String): Any? {
        return context().run { compile(script).run() }
    }

    override fun eval(reader: Reader): Any? {
        return eval(reader.readText())
    }

    override fun createBindings(): AsahiContext {
        return AsahiContext.create()
    }

    override fun getFactory(): ScriptEngineFactory {
        return factory
    }

    override fun compile(script: String, vararg namespaces: String): AsahiCompiledScript {
        val hash = (script.hashCode() + namespaces.hashCode())
        return if (cache.containsKey(hash)) {
            cache[hash]!!
        } else {
            AsahiCompiledScriptImpl(this, script).also { compiledScript ->
                AsahiLexer.of(script).addNamespaces(*namespaces).questAllTo(compiledScript)
            }.also { cache[hash] = it }
        }
    }

    override fun compile(tokens: Collection<String>, vararg namespaces: String): AsahiCompiledScript {
        val hash = tokens.hashCode() + namespaces.hashCode()
        return if (cache.containsKey(hash)) {
            cache[hash]!!
        } else {
            AsahiCompiledScriptImpl(this, tokens.joinToString(" ")).also { compiledScript ->
                AsahiLexer.of(tokens).addNamespaces(*namespaces).questAllTo(compiledScript)
            }.also { cache[hash] = it }
        }
    }

    override fun compile(file: File, vararg namespaces: String): AsahiCompiledScript {
        return compile(file.readLines(), *namespaces).also {
            AsahiEngineFactory add (file to it)
        }
    }

    override fun compile(script: String): AsahiCompiledScript {
        return compile(script, "common")
    }

    override fun compile(script: Reader): AsahiCompiledScript {
        return compile(script.readText(), "common")
    }

    override fun context(): AsahiContext {
        return context.getBindings(ScriptContext.ENGINE_SCOPE) as AsahiContext
    }

    override fun global(): AsahiContext {
        return context.getBindings(ScriptContext.GLOBAL_SCOPE) as AsahiContext
    }

    override fun invokeMethod(thiz: Any, name: String, vararg args: Any): Any? {
        return context().invoke(name, *args)
    }

    override fun invokeFunction(name: String, vararg args: Any): Any? {
        return context().invoke(name, *args)
    }

    override fun <T : Any?> getInterface(clasz: Class<T>?): T {
        TODO("Not yet implemented")
    }

    override fun <T : Any?> getInterface(thiz: Any?, clasz: Class<T>?): T {
        TODO("Not yet implemented")
    }

    companion object {
        fun create(factory: AsahiEngineFactory, context: AsahiContext = AsahiContextImpl.create()): AsahiEngine {
            return AsahiEngineImpl(factory, context)
        }
    }
}