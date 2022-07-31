package com.skillw.pouvoir.internal.script.javascript

import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.map.BaseMap
import com.skillw.pouvoir.api.script.ScriptTool
import com.skillw.pouvoir.api.script.engine.PouScriptEngine
import com.skillw.pouvoir.internal.manager.ScriptEngineManagerImpl
import com.skillw.pouvoir.internal.manager.ScriptEngineManagerImpl.addCheckVarsFunc
import com.skillw.pouvoir.internal.script.common.hook.ScriptBridge
import com.skillw.pouvoir.internal.script.common.top.TopLevel.putAllTopLevel
import com.skillw.pouvoir.internal.script.javascript.impl.NashornLegacy
import com.skillw.pouvoir.internal.script.javascript.impl.NashornNew
import taboolib.common.env.RuntimeDependencies
import taboolib.common.env.RuntimeDependency
import java.util.regex.Pattern
import javax.script.ScriptContext
import javax.script.ScriptEngine

@AutoRegister
@RuntimeDependencies(
    RuntimeDependency(
        "!org.openjdk.nashorn:nashorn-core:15.4",
        test = "!jdk.nashorn.api.scripting.NashornScriptEngineFactory"
    )
)
object PouJavaScriptEngine : PouScriptEngine() {
    override val key: String = "javascript"
    override val suffixes: Array<String> = arrayOf("js")
    override val functionPattern: Pattern = Pattern.compile("^function (?<name>.*)\\(.*\\)(| +)\\{$")
    override val bridge: ScriptBridge by lazy(LazyThreadSafetyMode.NONE) {
        try {
            Class.forName("jdk.nashorn.api.scripting.NashornScriptEngineFactory")
            NashornLegacy
        } catch (ex: ClassNotFoundException) {
            NashornNew
        }
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