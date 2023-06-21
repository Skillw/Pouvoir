package com.skillw.pouvoir.internal.core.script.javascript

import com.skillw.asahi.api.AsahiAPI
import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.Pouvoir.scriptEngineManager
import com.skillw.pouvoir.api.PouvoirAPI
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import com.skillw.pouvoir.api.script.PouFileCompiledScript
import com.skillw.pouvoir.api.script.engine.PouScriptEngine
import com.skillw.pouvoir.api.script.engine.hook.ScriptBridge
import com.skillw.pouvoir.internal.core.script.javascript.JSGlobal.importGlobalScripts
import com.skillw.pouvoir.internal.core.script.javascript.JSGlobal.reloadGlobalScripts
import com.skillw.pouvoir.internal.core.script.javascript.JSStaticClass.reloadStaticClasses
import com.skillw.pouvoir.internal.core.script.javascript.impl.NashornLegacy
import com.skillw.pouvoir.internal.core.script.javascript.impl.NashornNew
import com.skillw.pouvoir.util.*
import java.io.File
import java.util.regex.Pattern
import javax.script.Compilable
import javax.script.CompiledScript
import javax.script.ScriptContext
import javax.script.ScriptEngine

@AutoRegister
object PouJavaScriptEngine : PouScriptEngine() {
    override val key: String = "javascript"
    override val suffixes: Array<String> = arrayOf("js")
    override val functionPattern: Pattern = Pattern.compile("^function (?<name>.*)\\(.*\\)(| +)\\{$")
    private val jdkVersion = System.getProperty("java.specification.version").let {
        when (it) {
            "1.7" -> "7"
            "1.8" -> "8"
            else -> it
        }.toInt()
    }
    override val bridge: ScriptBridge by lazy {
        if (jdkVersion >= 11) NashornNew else NashornLegacy
    }

    override val engine: ScriptEngine
        get() = bridge.getEngine("-doe", "--global-per-engine", "--language=es6").apply {
            context.apply {
                getBindings(ScriptContext.ENGINE_SCOPE).apply {
                    putAll(scriptEngineManager.globalVariables)
                }
            }
        }

    override fun compile(script: String, vararg params: String): CompiledScript {
        return evalCache.map.computeIfAbsent(script) {
            (engine as Compilable).compile(("function main(${params.joinToString(",")}){$script\n}"))
                .apply { init() }
        }
    }

    override fun compile(file: File): PouFileCompiledScript? {
        val md5Hex = file.md5() ?: return null
        val normalizePath = file.pathNormalize()
        val md5Script = scriptsCache[normalizePath]
        if (md5Script != null && md5Hex == md5Script.md5) {
            return md5Script
        }
        val scriptLines = file.readLines()
        val script = (engine as Compilable).compile(scriptLines.toStringWithNext()).init()
        return PouFileCompiledScript(
            file,
            md5Hex,
            scriptLines,
            script,
            this
        ).also { scriptsCache[normalizePath] = it }
    }


    const val SCRIPT_OBJ = "this\$scriptObj"
    private fun CompiledScript.init(): CompiledScript {
        engine.importGlobalScripts()
        eval()
        engine.eval(
            """
                        
                        function NeigeNB(){}
                        NeigeNB.prototype = this
                        function ${SCRIPT_OBJ}(){
                          return new NeigeNB()
                         }
                          """.trimIndent()
        )
        return this
    }

    override fun onLoad() {
        scriptEngineManager.globalVariables.putAll(
            mapOf(
                "Bukkit" to org.bukkit.Bukkit::class.java.static(),
                "Arrays" to java.util.Arrays::class.java.static(),
                "Tool" to com.skillw.pouvoir.api.script.ScriptTool::class.java.static(),
                "Data" to com.skillw.pouvoir.api.script.ScriptTool::class.java.instance!!,
                "Pouvoir" to Pouvoir::class.java.static(),

                "ColorUtils" to com.skillw.pouvoir.util.script.ColorUtil::class.java.static(),
                "ItemUtils" to com.skillw.pouvoir.util.script.ItemUtil::class.java.static(),
                "MessageUtils" to com.skillw.pouvoir.util.script.MessageUtil::class.java.static(),

                "CalculationUtils" to "com.skillw.pouvoir.util.CalculationUtilKt".findClass()!!.static(),
                "MapUtils" to "com.skillw.pouvoir.util.MapUtilKt".findClass()!!.static(),
                "EntityUtils" to "com.skillw.pouvoir.util.EntityUtilKt".findClass()!!.static(),
                "FileUtils" to "com.skillw.pouvoir.util.FileUtilKt".findClass()!!.static(),
                "GsonUtils" to "com.skillw.pouvoir.util.GsonUtilKt".findClass()!!.static(),
                "NumberUtils" to "com.skillw.pouvoir.util.NumberUtilKt".findClass()!!.static(),
                "PlayerUtils" to "com.skillw.pouvoir.util.PlayerUtilKt".findClass()!!.static(),
                "ClassUtils" to "com.skillw.pouvoir.util.ClassUtilKt".findClass()!!.static(),
                "StringUtils" to "com.skillw.pouvoir.util.StringUtilKt".findClass()!!.static(),
                "PouvoirAPI" to PouvoirAPI::class.java.static(),
                "AsahiAPI" to AsahiAPI::class.java.static()
            )
        )
        onReload()
    }

    override fun onEnable() {
        onReload()
    }

    override fun onActive() {
        onReload()
    }

    override fun onReload() {
        reloadStaticClasses()
        reloadGlobalScripts()
    }

}