package com.skillw.pouvoir.api.script

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.Pouvoir.scriptManager
import com.skillw.pouvoir.api.plugin.ManagerTime
import com.skillw.pouvoir.api.plugin.map.BaseMap
import com.skillw.pouvoir.api.plugin.map.SingleExecMap
import com.skillw.pouvoir.api.plugin.map.component.Registrable
import com.skillw.pouvoir.api.script.annotation.ScriptAnnotationData
import com.skillw.pouvoir.api.script.engine.PouScriptEngine
import com.skillw.pouvoir.util.pathNormalize
import java.io.File
import javax.script.CompiledScript

// key = file.pathNormalize();
class PouFileCompiledScript(
    val file: File,
    val md5: String,
    private val scriptLines: List<String>,
    val script: CompiledScript,
    val pouEngine: PouScriptEngine,
) : Registrable<String> {

    override val key: String = file.pathNormalize()

    val functions = HashSet<String>()

    //function name to annotations
    internal val annotationData = BaseMap<String, Set<ScriptAnnotationData>>()

    private val fileAnnotations = HashSet<ScriptAnnotationData>()

    init {
        initAnnotation()
    }

    fun invoke(
        function: String = "main",
        arguments: Map<String, Any> = emptyMap(),
        vararg parameters: Any?,
    ): Any? {
        return pouEngine.bridge.invoke(script, function, arguments, *parameters)
    }

    private var lastHeadIndex = 0

    private fun initAnnotation() {
        for (index in scriptLines.indices) {
            val str = scriptLines[index]
            val matcher = pouEngine.functionPattern.matcher(str)
            if (!matcher.find()) continue
            val function = matcher.group("name")
            val annotations = getAnnotations(index, function)
            functions.add(function)
            annotationData[function] = annotations
        }

        fileAnnotations.addAll(
            getAnnotations(
                1,
                "null"
            ).filter { Pouvoir.scriptAnnotationManager[it.annotation]?.fileAnnotation == true })
    }


    private fun getAnnotations(
        index: Int,
        function: String,
    ): Set<ScriptAnnotationData> {
        val annotations = HashSet<ScriptAnnotationData>()
        var lastIndex = index
        while (lastIndex >= 1) {
            val last = scriptLines.getOrNull(--lastIndex) ?: break
            val matcher = pouEngine.getAnnotationPattern().matcher(last)
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

    private val onDelete = SingleExecMap()
    fun onDeleted(key: String, exec: (() -> Unit)) {
        onDelete += key to exec
    }


    private var deleted: Boolean = false

    fun delete() {
        deleted = true
        onDelete.forEach { it.value.invoke() }
    }

    private fun Set<ScriptAnnotationData>.handle() {
        data@ for (data in this) {
            val annotation = Pouvoir.scriptAnnotationManager[data.annotation] ?: continue@data
            if (annotation.awakeWhenEnable && !Pouvoir.plugin.isEnabled) {
                scriptManager.addExec(
                    ManagerTime.ENABLE,
                    data.script.key + "::" + data.function + "@" + data.annotation
                ) {
                    annotation.handle(data)
                }
                continue@data
            }
            annotation.handle(data)
        }
    }

    override fun register() {
        scriptManager.remove(key)?.delete()
        fileAnnotations.handle()
        annotationData.values.forEach { it.handle() }
        scriptManager[key] = this
    }
}