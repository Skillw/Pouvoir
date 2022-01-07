package com.skillw.pouvoir.api.script

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.Pouvoir.configManager
import com.skillw.pouvoir.Pouvoir.console
import com.skillw.pouvoir.Pouvoir.scriptEngine
import com.skillw.pouvoir.Pouvoir.scriptManager
import com.skillw.pouvoir.api.able.Keyable
import com.skillw.pouvoir.util.FileUtils
import com.skillw.pouvoir.util.MessageUtils.wrong
import taboolib.module.lang.sendLang
import java.io.File
import javax.script.CompiledScript
import javax.script.Invocable
import javax.script.ScriptException

class CompiledFile(val file: File) : Keyable<String> {

    override val key = FileUtils.nameNormalize(file)
    private var compiledScript: CompiledScript? = Pouvoir.compileManager.compileScript(file)

    fun canCompiled(): Boolean = compiledScript != null

    fun invoke(function: String, argsMap: MutableMap<String, Any> = HashMap(), vararg args: Any): Any? {
        if (compiledScript == null) {
            wrong("$key 's compiled script is null!")
            return null
        }
        val scriptEngine = scriptEngine
        argsMap.forEach { (key: String, value: Any) -> scriptEngine.put(key, value) }
        configManager.staticClasses
            .forEach { (key: String, value: Any) -> scriptEngine.put(key, value) }
        try {
            compiledScript!!.eval(scriptEngine.context)
        } catch (e: ScriptException) {
            console.sendLang("script-invoke-exception", function, key)
            e.printStackTrace()
        }
        val invocable: Invocable = scriptEngine as Invocable
        try {
            return invocable.invokeFunction(function, *args)
        } catch (e: ScriptException) {
            console.sendLang("script-invoke-script-exception", function, key)
            e.printStackTrace()
        } catch (e: Exception) {
            console.sendLang("script-invoke-exception", function, key)
            e.printStackTrace()
        }
        return null
    }

    override fun register() {
        if (compiledScript != null) {
            scriptManager.put(key, this)
        } else {
            compiledScript = Pouvoir.compileManager.compileScript(file)
            if (compiledScript != null) {
                scriptManager.put(key, this)
            } else {
                wrong("CompiledScript is null in $key!")
            }
        }
    }
}