package com.skillw.pouvoir.api.manager.sub.script

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.internal.script.common.PouCompiledScript
import java.io.File

/**
 * @className CompileManager
 * @author Glom
 * @date 2022/7/28 2:08
 * Copyright  2022 user. All rights reserved.
 */
abstract class CompileManager : Manager {
    abstract fun compile(file: File): PouCompiledScript?

    companion object {
        @JvmStatic
        fun File.compileScript(): PouCompiledScript? {
            return Pouvoir.compileManager.compile(this)
        }

        const val SCRIPT_OBJ = "this\$scriptObj"
    }
}