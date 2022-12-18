package com.skillw.pouvoir.internal.core.script.javascript

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.map.BaseMap
import com.skillw.pouvoir.api.script.ScriptTool
import com.skillw.pouvoir.api.script.engine.PouScriptEngine
import com.skillw.pouvoir.internal.core.script.common.hook.ScriptBridge
import com.skillw.pouvoir.internal.core.script.common.top.TopLevel.putAllTopLevel
import com.skillw.pouvoir.internal.core.script.javascript.impl.NashornLegacy
import com.skillw.pouvoir.internal.core.script.javascript.impl.NashornNew
import com.skillw.pouvoir.internal.manager.ScriptEngineManagerImpl
import com.skillw.pouvoir.internal.manager.ScriptEngineManagerImpl.addCheckVarsFunc
import com.skillw.pouvoir.util.ClassUtils.existClass
import taboolib.common.LifeCycle
import taboolib.common.env.RuntimeEnv
import taboolib.common.platform.Awake
import java.util.regex.Pattern
import javax.script.ScriptContext
import javax.script.ScriptEngine

@AutoRegister
object PouJavaScriptEngine : PouScriptEngine() {
    override val key: String = "javascript"
    override val suffixes: Array<String> = arrayOf("js")
    override val functionPattern: Pattern = Pattern.compile("^function (?<name>.*)\\(.*\\)(| +)\\{$")
    private val legacyNashorn
        get() = "jdk.nashorn.api.scripting.NashornScriptEngineFactory".existClass()
    private val newNashorn
        get() = "org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory".existClass()
    private val jdkVersion = System.getProperty("java.specification.version").let {
        when (it) {
            "1.7" -> "7"
            "1.8" -> "8"
            else -> it
        }.toInt()
    }
    override val bridge: ScriptBridge by lazy {
        if (newNashorn) NashornNew else NashornLegacy
    }

    @Awake(LifeCycle.CONST)
    fun loadNashorn() {
        if (!newNashorn && jdkVersion >= 15)
            RuntimeEnv.ENV.loadDependency("org.openjdk.nashorn:nashorn-core:15.4")
    }


    override val engine: ScriptEngine
        get() = bridge.getEngine("-doe", "--global-per-engine", "--language=es6").apply {
            context.apply {
                addCheckVarsFunc()
                getBindings(ScriptContext.ENGINE_SCOPE).apply {
                    putAll(ScriptEngineManagerImpl.globalVariables)
                    putAllTopLevel()
                    /*
                     为脚本提供快捷的顶级变量声明及调用
                     */
                    //储存所有的顶级变量声明函数的容器 key为变量名，value为函数
                    val globalDefFunc = BaseMap<String, () -> Any?>()

                    /*
                       第一次声明顶级变量时，调用该函数，并将函数放入容器中 key为变量名，value为函数
                     */
                    fun define(key: String, func: () -> Any?): Any {
                        return fun() { func()?.also { ScriptTool[key] = it } }
                            .also { globalDefFunc[key] = it }()
                    }

                    fun global(key: String): Any? {
                        return ScriptTool[key] ?: globalDefFunc[key]?.invoke()
                    }
                    put("define", ::define)
                    put("global", ::global)
                }
            }
        }


}