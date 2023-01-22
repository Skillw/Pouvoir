package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.manager.sub.script.CompileManager
import com.skillw.pouvoir.api.plugin.SubPouvoir
import com.skillw.pouvoir.api.script.PouFileCompiledScript
import com.skillw.pouvoir.api.script.engine.hook.PouCompiler
import com.skillw.pouvoir.internal.core.script.javascript.PouJavaScriptEngine
import com.skillw.pouvoir.util.*
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang
import java.io.File
import java.io.FileNotFoundException
import javax.script.CompiledScript
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource

internal object CompileManagerImpl : CompileManager() {

    override val key: String = "CompileManager"
    override val priority: Int = 7
    override val subPouvoir: SubPouvoir = Pouvoir

    @OptIn(ExperimentalTime::class)
    override fun compile(file: File): PouFileCompiledScript? {
        val suffix = file.extension
        val engine = Pouvoir.scriptEngineManager.getEngine(suffix)
        engine ?: kotlin.run {
            console().sendLang("script-engine-valid-suffix", suffix)
            return null
        }
        val mark = TimeSource.Monotonic.markNow()
        val path = file.pathNormalize()
        console().sendLang("script-compile-start", path)
        val script =
            try {
                engine.compile(file)
            } catch (e: FileNotFoundException) {
                console().sendLang("script-file-not-found", file.path)
                return null
            } catch (e: Throwable) {
                console().sendLang("script-compile-fail", file.path)
                e.printStackTrace()
                return null
            }
        val duration = mark.elapsedNow()
        console().sendLang("script-compile-end", path, duration)
        return script
    }

    override fun compile(script: String): CompiledScript {
        val compiler = values.firstOrNull { it.canCompile(script) } ?: PouJavaScriptEngine
        return compile(script, compiler)
    }

    override fun compile(script: String, engine: PouCompiler): CompiledScript {
        return engine.compile(script)
    }

}