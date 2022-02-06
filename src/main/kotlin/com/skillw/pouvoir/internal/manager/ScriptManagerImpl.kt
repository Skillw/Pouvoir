package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.Pouvoir.configManager
import com.skillw.pouvoir.Pouvoir.plugin
import com.skillw.pouvoir.Pouvoir.poolExecutor
import com.skillw.pouvoir.api.event.*
import com.skillw.pouvoir.api.manager.sub.script.ScriptManager
import com.skillw.pouvoir.api.script.CompiledFile
import com.skillw.pouvoir.util.FileUtils
import taboolib.common.platform.function.console
import taboolib.common5.FileWatcher
import taboolib.module.lang.sendLang
import java.io.File
import java.util.*
import java.util.regex.Pattern
import javax.script.CompiledScript
import javax.script.Invocable
import javax.script.ScriptEngine
import javax.script.ScriptException

object ScriptManagerImpl : ScriptManager() {
    override val key = "ScriptManager"
    override val priority = 8


    private val files = HashSet<File>()


    override fun addDir(file: File) {
        files.add(file)
        reloadFile()
    }

    private val pattern = Pattern.compile("(?<name>.*)::")

    override fun evalString(
        script: String,
        type: String,
        argsMap: MutableMap<String, Any>
    ): Any? {
        val pouScriptEngine = Pouvoir.scriptEngineManager[type]
        if (pouScriptEngine == null) {
            console().sendLang("script-engine-not-found", type)
            return null
        }
        val scriptEngine = pouScriptEngine.scriptEngine
        val bindings = scriptEngine.createBindings()
        bindings.putAll(argsMap)
        return scriptEngine.eval(script, bindings)
    }

    override fun invokeString(
        script: String,
        function: String,
        argsMap: MutableMap<String, Any>,
        vararg args: Any
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
        return invoke(
            Pouvoir.compileManager.compile(script.replace("$type::", ""), type) ?: return "null",
            function,
            argsMap,
            "native",
            *args
        )
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
        } catch (e: Exception) {
            console().sendLang("script-invoke-exception", function, key)
            e.printStackTrace()
        }
        return null
    }

    override fun hasScript(path: String): Boolean {
        return if (hasKey(path)) {
            true
        } else {
            map.filter { e -> e.key.endsWith(path) }.isNotEmpty()
        }
    }

    override fun init() {
        addDir(File(plugin.dataFolder, "scripts"))
    }

    override fun addScript(file: File) {
        CompiledFile(file).register()
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

    override fun search(path: String): Optional<CompiledFile> {
        if (hasKey(path)) {
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
                if (compiledScript != null) {
                    val file = File(configManager.serverFile, path)
                    return if (file.exists() && file.isFile) {
                        val compiledFile = CompiledFile(file)
                        if (compiledFile.canCompiled()) {
                            compiledFile.register()
                            Optional.of(compiledFile)
                        } else Optional.empty()
                    } else {
                        console().sendLang("script-compile-fail", path)
                        Optional.empty()
                    }
                } else {
                    console().sendLang("script-compile-fail", path)
                }
            }
            return Optional.of(optional.get().value)
        }
    }

    override fun get(key: String): CompiledFile? {
        val result = search(key)
        return if (result.isPresent) result.get() else null
    }

    override fun recompile(key: String): CompiledFile? {
        val compiledFile = get(key) ?: return null
        poolExecutor.execute {
            compiledFile.recompile()
            compiledFile.register()
        }
        return compiledFile
    }

    private fun addListenerIfNotExist(compiledFile: CompiledFile) {
        val file = compiledFile.file
        if (watcher.hasListener(file)) return
        watcher.addSimpleListener(file) {
            compiledFile.recompile()
            compiledFile.register()
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
            for (file in files) {
                FileUtils.listFiles(file).forEach {
                    val compiledFile = CompiledFile(it)
                    addListenerIfNotExist(compiledFile)
                    compiledFile.register()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun run(thing: String) {
        exec[thing]?.run {
            forEach { it() }
        }
    }

    override fun load() {
        val beforeEvent = ScriptLoadEvent(Time.BEFORE)
        beforeEvent.call()
        run("BeforeLoad")
        val afterEvent = ScriptLoadEvent(Time.AFTER)
        afterEvent.call()
        run("Load")
    }

    override fun enable() {
        val beforeEvent = ScriptEnableEvent(Time.BEFORE)
        beforeEvent.call()
        run("BeforeEnable")
        val afterEvent = ScriptEnableEvent(Time.AFTER)
        afterEvent.call()
        run("Enable")
    }

    override fun active() {
        val beforeEvent = ScriptActiveEvent(Time.BEFORE)
        beforeEvent.call()
        run("BeforeActive")
        val afterEvent = ScriptActiveEvent(Time.AFTER)
        afterEvent.call()
        run("Active")
    }


    override fun reload() {
        val beforeEvent = ScriptReloadEvent(Time.BEFORE)
        beforeEvent.call()
        exec.clear()
        poolExecutor.execute {
            reloadFile()
            run("BeforeReload")
            val afterEvent = ScriptReloadEvent(Time.AFTER)
            afterEvent.call()
            run("Reload")
        }
    }

    override fun disable() {
        val beforeEvent = ScriptDisableEvent(Time.BEFORE)
        beforeEvent.call()
        run("BeforeDisable")

        val afterEvent = ScriptDisableEvent(Time.AFTER)
        afterEvent.call()
        run("Disable")
    }


}