package com.skillw.asahi.internal.script

import com.skillw.asahi.api.member.context.AsahiContext
import com.skillw.asahi.api.member.quest.Quester
import com.skillw.asahi.api.script.AsahiCompiledScript
import com.skillw.asahi.api.script.AsahiEngine
import com.skillw.asahi.api.script.AsahiScriptException
import com.skillw.pouvoir.util.script.MessageUtil.debug
import java.util.*
import javax.script.ScriptContext
import javax.script.ScriptContext.ENGINE_SCOPE
import javax.script.ScriptEngine

/**
 * @className AsahiCompiledScriptImpl
 *
 * @author Glom
 * @date 2022/12/28 21:26 Copyright 2022 user.
 */
class AsahiCompiledScriptImpl internal constructor(engine: AsahiEngine, val raw: String) : AsahiCompiledScript(engine) {
    private val questers = ArrayList<Quester<*>>()
    private var isExitFunc: AsahiContext.() -> Boolean = { isExit() }
    override fun isExit(isExit: AsahiContext.() -> Boolean): AsahiCompiledScript {
        this.isExitFunc = isExit
        return this
    }

    override fun rawScript(): String {
        return raw
    }

    @Throws(AsahiScriptException::class)
    override fun AsahiContext.execute(): Any? {
        return questers.runCatching result@{
            var previous: Any? = null
            forEachIndexed { index, exec ->
                ifDebug {
                    debug("$index - $exec")
                }
                if (isExitFunc()) return@result previous.ifDebug { debug("Exit: $it") }
                exec.run()
                    .ifDebug(::debug)
                    .also {
                        previous = it
                    }
            }
            return@result previous.ifDebug { last -> debug("Return: $last") }.also { debugOff() }
        }.run {
            if (isSuccess) getOrThrow()
            else exceptionOrNull()?.also { exit();it.printStackTrace() }
        }
    }

    override fun add(quester: Quester<Any?>) {
        questers.add(quester)
    }

    operator fun plusAssign(quester: Quester<Any?>) {
        add(quester)
    }

    override fun eval(): Any? {
        val context = engine.context.getBindings(ENGINE_SCOPE) as AsahiContext
        return context.execute()
    }

    override fun eval(context: ScriptContext): Any {
        return engine.context.getBindings(ENGINE_SCOPE).putAll(context.getBindings(ENGINE_SCOPE) as AsahiContext)
    }

    override fun getEngine(): ScriptEngine {
        return engine
    }

}