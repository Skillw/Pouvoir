package com.skillw.pouvoir.internal.function.reader

import com.skillw.pouvoir.Pouvoir.pouFunctionManager
import com.skillw.pouvoir.api.function.reader.IReader
import com.skillw.pouvoir.api.map.KeyMap
import com.skillw.pouvoir.internal.function.common.PouFunc
import com.skillw.pouvoir.internal.function.context.SimpleContext
import com.skillw.pouvoir.internal.function.functions.compile.DefFunction
import java.io.FileReader

/**
 * @className FileReader
 *
 * @author Glom
 * @date 2022/8/1 8:59 Copyright 2022 user. All rights reserved.
 */
class FileReader(private val reader: SimpleReader) : IReader by reader {
    val global = SimpleContext()
    val functions = KeyMap<String, PouFunc>()

    constructor(reader: FileReader) : this(SimpleReader(reader.readText()))

    init {
        pouFunctionManager.parse(this) {
            this["function"] = DefFunction
        }
    }
}