package com.skillw.pouvoir.internal.engine

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.script.CompiledFile
import com.skillw.pouvoir.api.script.annotation.ScriptAnnotationData
import com.skillw.pouvoir.api.script.engine.PouScriptEngine
import org.codehaus.groovy.jsr223.GroovyScriptEngineFactory
import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory
import taboolib.common.env.RuntimeDependencies
import taboolib.common.env.RuntimeDependency
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.regex.Pattern

private val jsFunctionPattern = Pattern.compile("^function (?<name>.*)\\(.*\\)(| +)\\{$")

@RuntimeDependencies(
    RuntimeDependency(
        "!org.openjdk.nashorn:nashorn-core:15.3",
        test = "!jdk.nashorn.api.scripting.NashornScriptEngineFactory"
    )
)
object JavaScriptEngine : PouScriptEngine(
    "javascript", try {
        System.setProperty("nashorn.args", "--language=es6")
        Class.forName("jdk.nashorn.api.scripting.NashornScriptEngineFactory")
        jdk.nashorn.api.scripting.NashornScriptEngineFactory().getScriptEngine(Pouvoir::class.java.classLoader)
    } catch (ex: ClassNotFoundException) {
        NashornScriptEngineFactory().getScriptEngine(Pouvoir::class.java.classLoader)
    }, {
        handleMap(it, jsFunctionPattern)
    }, "js"
) {
    override fun addFunctionStructure(script: String, name: String): String {
        return "function $name(){$script}"
    }
}

private val groovyFunctionPattern = Pattern.compile("^def (?<name>.*)\\(.*\\)(| +)\\{\$")

@RuntimeDependencies(
    RuntimeDependency("org.codehaus.groovy:groovy-jsr223:3.0.9"),
    RuntimeDependency("org.codehaus.groovy:groovy:3.0.9")
)
object GroovyScriptEngine : PouScriptEngine(
    "groovy", GroovyScriptEngineFactory().scriptEngine,
    {
        handleMap(it, groovyFunctionPattern)
    },
    "groovy"
) {
    override fun addFunctionStructure(script: String, name: String): String {
        return "def $name(){$script}"
    }
}

private fun handleMap(
    pair: Pair<CompiledFile, List<String>>,
    pattern: Pattern
): MutableMap<String, LinkedList<ScriptAnnotationData>> {
    val compiledFile = pair.first
    val scripts = pair.second
    val map = ConcurrentHashMap<String, LinkedList<ScriptAnnotationData>>()
    for (index in scripts.indices) {
        val str = scripts[index]
        val matcher = pattern.matcher(str)
        if (!matcher.find()) {
            continue
        }
        val function = matcher.group("name")
        val annotations = getAnnotations(index, compiledFile, scripts, function)
        map[function] = annotations
    }
    return map
}

private val scriptAnnotationPattern: Pattern = Pattern.compile("//@(?<key>.*)\\((?<args>.*)\\)", 2)

private fun getAnnotations(
    index: Int,
    compiledFile: CompiledFile,
    scripts: List<String>,
    function: String
): LinkedList<ScriptAnnotationData> {
    val annotations = LinkedList<ScriptAnnotationData>()
    var lastIndex = index
    while (lastIndex >= 1) {
        val last = scripts[--lastIndex]
        val matcher = scriptAnnotationPattern.matcher(last)
        if (!matcher.find()) {
            if (last.contains("@")) continue;
            break
        }
        annotations.add(
            ScriptAnnotationData(
                matcher.group("key"),
                compiledFile,
                function,
                matcher.group("args")
            )
        )
    }
    return annotations
}

