package com.skillw.pouvoir.api.manager.sub.script

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.internal.core.script.common.PouCompiledScript
import java.io.File
import javax.script.CompiledScript

/**
 * @className CompileManager
 *
 * @author Glom
 * @date 2022/7/28 2:08 Copyright 2022 user. All rights reserved.
 */
abstract class CompileManager : Manager {

    /**
     * 添加全局脚本文件
     *
     * @param file 文件夹/js脚本文件
     */
    abstract fun addGlobal(file: File)

    /**
     * 删除全局脚本文件
     *
     * @param file 文件夹/js脚本文件
     */
    abstract fun removeGlobal(file: File)

    /**
     * Compile
     *
     * @param file 你需要编译的文件
     * @return 预编译脚本
     */
    abstract fun compile(file: File): PouCompiledScript?

    companion object {
        @JvmStatic
        fun File.compileScript(): PouCompiledScript? {
            return Pouvoir.compileManager.compile(this)
        }

        const val SCRIPT_OBJ = "this\$scriptObj"
    }

    abstract fun compile(script: String): CompiledScript
}