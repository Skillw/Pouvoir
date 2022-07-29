package com.skillw.pouvoir.api.script.annotation

import com.skillw.pouvoir.internal.script.common.PouCompiledScript

data class ScriptAnnotationData(
    val annotation: String,
    val script: PouCompiledScript,
    val function: String,
    val args: String,
)
