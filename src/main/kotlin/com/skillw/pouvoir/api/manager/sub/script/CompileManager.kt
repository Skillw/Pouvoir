package com.skillw.pouvoir.api.manager.sub.script

import com.skillw.pouvoir.api.manager.Manager
import java.io.File
import javax.script.CompiledScript

interface CompileManager : Manager {
    fun compileScript(file: File): CompiledScript?
    fun compileScript(path: String): CompiledScript?
}