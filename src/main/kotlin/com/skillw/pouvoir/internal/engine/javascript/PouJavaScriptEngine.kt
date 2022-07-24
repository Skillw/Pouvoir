package com.skillw.pouvoir.internal.engine.javascript

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.map.KeyMap
import com.skillw.pouvoir.api.script.PouCompiledScript
import com.skillw.pouvoir.api.script.engine.PouScriptEngine
import com.skillw.pouvoir.util.FileUtils.md5
import com.skillw.pouvoir.util.FileUtils.pathNormalize
import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory
import taboolib.common.env.RuntimeDependencies
import taboolib.common.env.RuntimeDependency
import java.io.File
import java.util.regex.Pattern
import javax.script.Compilable
import javax.script.ScriptEngine

@AutoRegister
@RuntimeDependencies(
    RuntimeDependency(
        "!org.openjdk.nashorn:nashorn-core:15.3",
        test = "!jdk.nashorn.api.scripting.NashornScriptEngineFactory"
    )
)
object PouJavaScriptEngine : PouScriptEngine() {
    override val key: String = "javascript"
    override val suffixes: Array<String> = arrayOf("js")
    override val functionPattern: Pattern = Pattern.compile("^function (?<name>.*)\\(.*\\)(| +)\\{$")
    internal lateinit var nashorn: String
    private val creator: NashornEngineCreator by lazy(LazyThreadSafetyMode.NONE) {
        try {
            Class.forName("jdk.nashorn.api.scripting.NashornScriptEngineFactory")
            nashorn = "legacy"
            NashornEngineCreator {
                jdk.nashorn.api.scripting.NashornScriptEngineFactory()
                    .getScriptEngine(it, Pouvoir::class.java.classLoader)
            }
        } catch (ex: ClassNotFoundException) {
            nashorn = "new"
            NashornEngineCreator {
                NashornScriptEngineFactory().getScriptEngine(it, Pouvoir::class.java.classLoader)
            }
        }
    }

    override val engine: ScriptEngine by lazy(LazyThreadSafetyMode.NONE) {
        creator.getEngine("-doe", "--global-per-engine", "--language=es6")
    }

    private val scripts = KeyMap<String, PouJavaScript>()

    override fun compile(file: File): PouCompiledScript? {
        val md5Hex = file.md5() ?: return null
        val normalizePath = file.pathNormalize()
        val md5Script = scripts[normalizePath]
        if (md5Script != null && md5Hex == md5Script.md5) {
            return md5Script
        }
        val compiledScript = (engine as Compilable).compile(file.readText())
        return PouJavaScript(file, md5Hex, compiledScript).also { scripts[normalizePath] = it }

    }
}