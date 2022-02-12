package com.skillw.pouvoir.api.script

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.Pouvoir.scriptManager
import com.skillw.pouvoir.api.able.Keyable
import com.skillw.pouvoir.api.manager.Manager.Companion.addSingle
import com.skillw.pouvoir.api.map.SingleExecMap
import com.skillw.pouvoir.api.plugin.SubPouvoir
import com.skillw.pouvoir.api.script.annotation.ScriptAnnotationData
import com.skillw.pouvoir.api.script.engine.PouScriptEngine
import com.skillw.pouvoir.internal.engine.JavaScriptEngine
import com.skillw.pouvoir.util.FileUtils
import com.skillw.pouvoir.util.MessageUtils.wrong
import java.io.File
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import javax.script.CompiledScript

class CompiledFile(val file: File, val subPouvoir: SubPouvoir) : Keyable<String>, SingleExecMap() {

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

    init {
        init()
    }

    private fun init() {
        recompile()
        if (compiledScript == null) {
            wrong("CompiledScript is null in $key!")
            return
        }
        val script = file.readLines()
        annotations.clear()
        pouScriptEngine.getAnnotationData(this to script).forEach {
            annotations[it.key] = it.value
        }
    }

    fun canCompiled(): Boolean = compiledScript != null


    fun invoke(function: String, argsMap: MutableMap<String, Any> = HashMap(), vararg args: Any): Any? {
        if (compiledScript == null) {
            wrong("$key 's compiled script is null!")
            return null
        }
        return scriptManager.invoke(compiledScript!!, function, argsMap, key, *args)
    }

    override fun run(thing: String) {
        this.filter {
            it.key.startsWith(thing.lowercase())
        }.forEach {
            it.value.invoke()
            this.remove(it.key)
        }
    }

    private fun recompile() {
        run("recompile")
        compiledScript = Pouvoir.compileManager.compileFile(file)
    }

    fun reload() {
        run("reload")
        init()
        register()
    }

    override fun register() {
        run("register")
        if (compiledScript == null) {
            wrong("$key 's compiled script is null!")
            return
        }
        annotations.values.forEach {
            data@ for (data in it) {
                val annotation = Pouvoir.scriptAnnotationManager[data.annotation] ?: continue@data
                if (annotation.awakeWhenEnable && !subPouvoir.plugin.isEnabled) {
                    scriptManager.addSingle("Enable") {
                        annotation.handle(data)
                    }
                    continue@data
                }
                annotation.handle(data)
            }
        }
        scriptManager.put(key, this)
    }
}