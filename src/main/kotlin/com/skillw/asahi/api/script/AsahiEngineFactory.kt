package com.skillw.asahi.api.script

import java.io.File
import java.util.concurrent.ConcurrentHashMap
import javax.script.ScriptEngineFactory

/**
 * @className AsahiEngineFactory
 *
 * @author Glom
 * @date 2022/12/24 13:04 Copyright 2022 user. All rights reserved.
 */
class AsahiEngineFactory : ScriptEngineFactory {
    private val names = mutableListOf("com/skillw/asahi")
    private val version = "0.0.1-alpha"
    private val mimeTypes = mutableListOf("")
    private val extensions = mutableListOf("com/skillw/asahi")
    override fun getEngineName(): String = "Asahi Engine"

    override fun getEngineVersion(): String = version

    override fun getExtensions(): MutableList<String> = extensions

    override fun getMimeTypes(): MutableList<String> = mimeTypes

    override fun getNames(): MutableList<String> = names

    override fun getLanguageName(): String = names[0]

    override fun getLanguageVersion(): String = version

    override fun getParameter(key: String?): Any? {
        return when (key) {
            "javax.script.name" -> names[0]
            "javax.script.engine" -> engineName
            "javax.script.engine_version" -> engineVersion
            "javax.script.language" -> languageName
            "javax.script.language_version" -> languageVersion
            "THREADING" -> null
            else -> null
        }
    }

    override fun getMethodCallSyntax(obj: String, method: String, vararg args: String): String {
        val sb = StringBuilder().append(obj).append(' ').append(method).append(' ')
        val len = args.size
        if (len > 0) {
            sb.append(args[0])
        }
        for (i in 1 until len) {
            sb.append(' ').append(args[i])
        }
        sb.append(' ')
        return sb.toString()
    }

    override fun getOutputStatement(toDisplay: String): String {
        return "print $toDisplay "
    }

    override fun getProgram(vararg statements: String): String {
        val sb = StringBuilder()
        val var3: Array<out String> = statements
        val var4 = statements.size
        for (var5 in 0 until var4) {
            val statement = var3[var5]
            sb.append(statement).append(';')
        }
        return sb.toString()
    }

    override fun getScriptEngine(): AsahiEngine {
        return AsahiEngine.create(this)
    }

    companion object {
        private val fileToScript = ConcurrentHashMap<File, AsahiCompiledScript>()

        @JvmStatic
        infix fun add(pair: Pair<File, AsahiCompiledScript>) {
            fileToScript += pair
        }

        @JvmStatic
        fun addScript(file: File, script: AsahiCompiledScript) {
            fileToScript += file to script
        }

        @JvmStatic
        fun getScript(file: File): AsahiCompiledScript? {
            return fileToScript[file]
        }

        @JvmStatic
        fun search(path: String): AsahiCompiledScript? {
            val file = File(path)
            if (fileToScript.containsKey(file)) return fileToScript[file]
            fileToScript.keys
                .firstOrNull { it.name.endsWith(path) }
                ?.let {
                    return fileToScript[it]
                }
            return null
        }
    }
}