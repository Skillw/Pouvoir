package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.Pouvoir.configManager
import com.skillw.pouvoir.Pouvoir.plugin
import com.skillw.pouvoir.Pouvoir.scriptEngineManager
import com.skillw.pouvoir.api.manager.sub.script.ScriptManager
import com.skillw.pouvoir.api.plugin.SubPouvoir
import com.skillw.pouvoir.api.script.CompiledFile
import com.skillw.pouvoir.util.FileUtils
import com.skillw.pouvoir.util.MapUtils.addSingle
import taboolib.common.platform.function.console
import taboolib.common5.FileWatcher
import taboolib.module.lang.sendLang
import java.io.File
import java.io.FileNotFoundException
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.regex.Pattern
import java.util.regex.Pattern.MULTILINE
import javax.script.CompiledScript
import javax.script.Invocable
import javax.script.ScriptEngine
import javax.script.ScriptException

object ScriptManagerImpl : ScriptManager() {
    override val key = "ScriptManager"
    override val priority = 8
    override val subPouvoir = Pouvoir

    private val fileMap = ConcurrentHashMap<SubPouvoir, HashSet<File>>()


    override fun addDir(file: File, subPouvoir: SubPouvoir) {
        if (fileMap[subPouvoir]?.contains(file) != true) {
            fileMap.addSingle(subPouvoir, file)
            reloadFile()
            return
        }
        reloadDir(file, subPouvoir)
    }

    override fun reloadDir(file: File, subPouvoir: SubPouvoir) {
        if (fileMap[subPouvoir]?.contains(file) != true) addDir(file, subPouvoir)
        else {
            val path = FileUtils.pathNormalize(file)
            this.filter { it.key.startsWith(path) }.forEach {
                it.value.reload()
            }
        }
    }

    override fun evalStringQuickly(
        script: String,
        type: String,
        argsMap: MutableMap<String, Any>
    ): Any? {
        val pouScriptEngine = scriptEngineManager[type] ?: scriptEngineManager.getEngine(type)
        pouScriptEngine ?: kotlin.run {
            console().sendLang("script-engine-not-found", type)
            return null
        }
        return evalString(pouScriptEngine.addFunctionStructure(script, "main") + "main()", type, argsMap)
    }


    override fun evalString(
        script: String,
        type: String,
        argsMap: MutableMap<String, Any>
    ): Any? {
        val pouScriptEngine = scriptEngineManager[type] ?: scriptEngineManager.getEngine(type)
        pouScriptEngine ?: kotlin.run {
            console().sendLang("script-engine-not-found", type)
            return null
        }
        val scriptEngine = pouScriptEngine.scriptEngine
        val bindings = scriptEngine.createBindings()
        bindings.putAll(argsMap)
        return scriptEngine.eval(script, bindings)
    }

    private val pattern = Pattern.compile("(?<name>.*?)::")
    override fun evalString(
        script: String,
        argsMap: MutableMap<String, Any>
    ): Any? {
        val type = try {
            val matcher = pattern.matcher(script.split("\n")[0])
            if (matcher.find()) {
                matcher.group("name")
            } else {
                "javascript"
            }
        } catch (throwable: Throwable) {
            "javascript"
        }
        return evalString(relocate(script.replaceFirst("$type::", "")), type, argsMap)
    }

    override fun addStatic(scriptEngine: ScriptEngine): ScriptEngine {
        configManager.staticClasses
            .forEach { (key: String, value: Any) -> scriptEngine.put(key, value) }
        return scriptEngine
    }

    override fun addParams(
        scriptEngine: ScriptEngine,
        argsMap: MutableMap<String, Any>
    ): ScriptEngine {
        argsMap.forEach { (key: String, value: Any) -> scriptEngine.put(key, value) }
        return scriptEngine
    }


    override fun invoke(
        script: CompiledScript,
        function: String,
        argsMap: MutableMap<String, Any>,
        key: String,
        vararg args: Any
    ): Any? {
        val engine = addParams(script.engine, argsMap)
        try {
            script.eval(engine.context)
            return (engine as Invocable).invokeFunction(function, *args)
        } catch (e: ScriptException) {
            console().sendLang("script-invoke-script-exception", function, key)
            e.printStackTrace()
        } catch (e: FileNotFoundException) {
            console().sendLang("script-file-not-found", key)
        } catch (e: Exception) {
            console().sendLang("script-invoke-exception", function, key)
            e.printStackTrace()
        }
        return null
    }

    override fun hasScript(path: String): Boolean {
        return if (containsKey(path)) {
            true
        } else {
            map.filter { e -> e.key.endsWith(path) }.isNotEmpty()
        }
    }

    override fun onInit() {
        addDir(File(plugin.dataFolder, "scripts"), Pouvoir)
    }

    override fun addScript(file: File, subPouvoir: SubPouvoir) {
        CompiledFile(file, subPouvoir).register()
    }

    override fun invoke(
        path: String,
        function: String,
        argsMap: MutableMap<String, Any>,
        vararg args: Any
    ): Any? {
        val compiledFile = get(path) ?: return null
        return compiledFile.invoke(function, argsMap, *args)
    }

    override fun invoke(string: String, argsMap: MutableMap<String, Any>): Any? {
        if (string.contains("\n")) {
            return evalString(string, argsMap)
        } else if (string.contains("::")) {
            return invokePathWithFunction(string, argsMap)
        }
        return null
    }

    override fun invokePathWithFunction(
        pathWithFunction: String,
        argsMap: MutableMap<String, Any>,
        vararg args: Any
    ): Any? {
        val strings = pathWithFunction.split("::").toTypedArray()
        return if (strings.size == 2) {
            val key = strings[0]
            val function = strings[1]
            this.invoke(key, function, args = args, argsMap = argsMap)
        } else "!wrong-format!"
    }

    override fun search(path: String, subPouvoir: SubPouvoir): Optional<CompiledFile> {
        if (containsKey(path)) {
            return Optional.ofNullable(map[path])
        } else {
            val optional = this.map
                .entries
                .stream()
                .filter { (key, _) ->
                    key.endsWith(path)
                }
                .findFirst()
            if (!optional.isPresent) {
                val compiledScript: CompiledScript? = Pouvoir.compileManager.compileFile(path)
                compiledScript?.also {
                    val file = File(configManager.serverFile, path)
                    return if (file.exists() && file.isFile) {
                        val compiledFile = CompiledFile(file, subPouvoir)
                        if (compiledFile.canCompiled) {
                            compiledFile.register()
                            Optional.of(compiledFile)
                        } else Optional.empty()
                    } else {
                        console().sendLang("script-compile-fail", path)
                        Optional.empty()
                    }
                } ?: kotlin.run { console().sendLang("script-compile-fail", path) }
            }
            return try {
                Optional.of(optional.get().value)
            } catch (e: Exception) {
                Optional.ofNullable(null)
            }
        }
    }

    override fun get(key: String): CompiledFile? {
        val result = search(key)
        return if (result.isPresent) result.get() else null
    }

    private fun addListenerIfNotExist(compiledFile: CompiledFile) {
        val file = compiledFile.file
        if (watcher.hasListener(file)) return
        watcher.addSimpleListener(file) {
            compiledFile.reload()
        }
    }

    private val watcher: FileWatcher = FileWatcher.INSTANCE
    private fun reloadFile() {
        try {
            map.values.forEach {
                if (watcher.hasListener(it.file)) {
                    watcher.removeListener(it.file)
                }
            }
            this.map.clear()
            for ((subPouvoir, files) in fileMap) {
                for (file in files)
                    FileUtils.listFiles(file).forEach {
                        val compiledFile = CompiledFile(it, subPouvoir)
                        addListenerIfNotExist(compiledFile)
                        compiledFile.register()
                    }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

//    override fun onEnable() {
//        onReload()
//    }

    override fun onReload() {
        subPouvoir.managerData.exec[this]?.clear()
        reloadFile()
    }

    override fun onDisable() {

    }

    private val relocate: Pattern = Pattern.compile("^import (?!!)(?<package>taboolib\\..*)$", MULTILINE)
    override fun relocate(script: String): String {
        val matcher = relocate.matcher(script)
        val builder = StringBuffer()
        while (matcher.find()) {
            val path = matcher.group("package")
            matcher.appendReplacement(builder, "import com.skillw.pouvoir.$path")
        }
        return matcher.appendTail(builder).toString()
    }

}