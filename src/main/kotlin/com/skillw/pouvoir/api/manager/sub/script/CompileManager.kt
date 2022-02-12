package com.skillw.pouvoir.api.manager.sub.script

import com.skillw.pouvoir.api.manager.Manager
import java.io.File
import javax.script.CompiledScript

interface CompileManager : Manager {
    fun compileFile(file: File): CompiledScript?
    fun compileFile(path: String): CompiledScript?
}