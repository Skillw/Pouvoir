package com.skillw.pouvoir.api.script.engine.hook

import com.skillw.pouvoir.api.plugin.map.component.Registrable
import com.skillw.pouvoir.api.script.PouFileCompiledScript
import java.io.File
import javax.script.CompiledScript

/**
 * @className PouCompiler
 *
 * @author Glom
 * @date 2023/1/20 9:33 Copyright 2024 Glom.
 */
interface PouCompiler : Registrable<String> {
    fun compile(file: File): PouFileCompiledScript?
    fun compile(script: String, vararg params: String): CompiledScript
    fun canCompile(script: String): Boolean
}