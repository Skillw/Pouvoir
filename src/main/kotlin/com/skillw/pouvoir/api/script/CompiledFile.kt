package com.skillw.pouvoir.api.script

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.Pouvoir.scriptManager
import com.skillw.pouvoir.api.able.Keyable
import com.skillw.pouvoir.api.script.annotation.ScriptAnnotationData
import com.skillw.pouvoir.api.script.engine.PouScriptEngine
import com.skillw.pouvoir.internal.engine.JavaScriptEngine
import com.skillw.pouvoir.util.FileUtils
import com.skillw.pouvoir.util.MapUtils.addSingle
import com.skillw.pouvoir.util.MessageUtils.wrong
import java.io.File
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import javax.script.CompiledScript

class CompiledFile(val file: File) : Keyable<String> {
    override val key = FileUtils.pathNormalize(file)

    private val pouScriptEngine: PouScriptEngine by lazy {
        val engine = Pouvoir.scriptEngineManager.getEngine(file.extension)
        if (engine != null)
            engine
        else {
            wrong("Pouvoir hasn't supported the script files with extension ${file.extension}!")
            JavaScriptEngine
        }
    }
    private var compiledScript: CompiledScript? = Pouvoir.compileManager.compileFile(file)
    val annotations = ConcurrentHashMap<String, LinkedList<ScriptAnnotationData>>()

    val functions by lazy {
        val set = HashSet<String>()
        set.addAll(annotations.keys().toList())
        return@lazy set
    }

    fun canCompiled(): Boolean = compiledScript != null

    init {
        val script = file.readLines()
        pouScriptEngine.functions(this to script).forEach {
            annotations[it.key] = it.value
        }
    }

    fun invoke(function: String, argsMap: MutableMap<String, Any> = HashMap(), vararg args: Any): Any? {
        if (compiledScript == null) {
            wrong("$key 's compiled script is null!")
            return null
        }
        return scriptManager.invoke(compiledScript!!, function, argsMap, key, *args)
    }


    fun recompile() {
        compiledScript = Pouvoir.compileManager.compileFile(file)
    }

    override fun register() {
        if (compiledScript != null) {
            scriptManager.put(key, this)
        } else {
            compiledScript = Pouvoir.compileManager.compileFile(file)
            if (compiledScript != null) {
                scriptManager.put(key, this)
            } else {
                wrong("CompiledScript is null in $key!")
            }
        }
        annotations.values.forEach {
            data@ for (data in it) {
                val annotation = Pouvoir.scriptAnnotationManager[data.annotation] ?: continue@data
                if (annotation.awakeWhenEnable && !Pouvoir.plugin.isEnabled) {
                    scriptManager.exec.addSingle("Enable") {
                        annotation.handle(data)
                    }
                    continue@data
                }
                annotation.handle(data)
            }
        }
    }
}