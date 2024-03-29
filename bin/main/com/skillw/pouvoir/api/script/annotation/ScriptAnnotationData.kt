package com.skillw.pouvoir.api.script.annotation

import com.skillw.pouvoir.api.script.PouFileCompiledScript


/**
 * Script annotation data
 *
 * @constructor Create empty Script annotation data
 * @property annotation 脚本注解对象
 * @property script 预编译脚本对象
 * @property function 函数名
 * @property args 参数
 */
data class ScriptAnnotationData(
    val annotation: String,
    val script: PouFileCompiledScript,
    val function: String,
    val args: String,
)
