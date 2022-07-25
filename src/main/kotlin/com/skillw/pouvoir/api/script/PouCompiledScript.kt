package com.skillw.pouvoir.api.script

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.Pouvoir.scriptManager
import com.skillw.pouvoir.api.able.Registrable
import com.skillw.pouvoir.api.event.ManagerTime
import com.skillw.pouvoir.api.manager.Manager.Companion.addExec
import com.skillw.pouvoir.api.map.BaseMap
import com.skillw.pouvoir.api.script.annotation.ScriptAnnotationData
import com.skillw.pouvoir.api.script.engine.PouScriptEngine
import com.skillw.pouvoir.util.FileUtils.pathNormalize
import java.io.File
import java.util.concurrent.ConcurrentHashMap

// key = file.pathNormalize();
abstract class PouCompiledScript(val file: File, val scripts: List<String>, val engine: PouScriptEngine) :
    Registrable<String> {
    //this annotations
    var scriptAnnotations = HashSet<ScriptAnnotationData>()

    //function name to annotations
    val annotationData = BaseMap<String, Set<ScriptAnnotationData>>()
    override val key: String = file.pathNormalize()

    abstract fun invoke(
        function: String = "main",
        variables: Map<String, Any> = emptyMap(),
        vararg arguments: Any?
    ): Any?

    init {
        annotationData.putAll(initAnnotation())
    }

    private var lastHeadIndex = 0

    private fun initAnnotation(): Map<String, Set<ScriptAnnotationData>> {
        val map = ConcurrentHashMap<String, Set<ScriptAnnotationData>>()
        if (scripts.isEmpty()) return map
        for (index in scripts.indices) {
            val str = scripts[index]
            val matcher = engine.functionPattern.matcher(str)
            if (!matcher.find()) continue
            val function = matcher.group("name")
            val annotations = getAnnotations(index, function)
            map[function] = annotations
        }

        scriptAnnotations.addAll(getAnnotations(lastHeadIndex, "null"))

        return map
    }

    private fun getAnnotations(
        index: Int,
        function: String
    ): Set<ScriptAnnotationData> {
        val annotations = HashSet<ScriptAnnotationData>()
        var lastIndex = index
        while (lastIndex >= 1) {
            val last = scripts[--lastIndex]
            val matcher = engine.getAnnotationPattern().matcher(last)
            if (!matcher.find()) {
                if (lastHeadIndex == 0)
                    lastHeadIndex = lastIndex
                break
            }
            annotations.add(
                ScriptAnnotationData(
                    matcher.group("key"),
                    this,
                    function,
                    matcher.group("args")
                )
            )
        }
        return annotations
    }

    private val execs = HashSet<() -> Unit>()
    fun onRemove(exec: (() -> Unit)) {
        execs += exec
    }

    var remove: Boolean = false
    fun remove() {
        remove = true
        execs.forEach { it.invoke() }
    }

    private fun Set<ScriptAnnotationData>.process() {
        data@ for (data in this) {
            val annotation = Pouvoir.scriptAnnotationManager[data.annotation] ?: continue@data
            if (annotation.awakeWhenEnable && !Pouvoir.plugin.isEnabled) {
                scriptManager.addExec(
                    data.script.key + "::" + data.function + "@" + data.annotation,
                    ManagerTime.ENABLE
                ) {
                    annotation.handle(data)
                }
                continue@data
            }
            annotation.handle(data)
        }
    }

    override fun register() {
        scriptAnnotations.process()
        annotationData.values.forEach { it.process() }
        scriptManager[key] = this
    }
}