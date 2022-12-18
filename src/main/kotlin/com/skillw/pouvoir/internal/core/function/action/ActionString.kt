package com.skillw.pouvoir.internal.core.function.action

import com.skillw.pouvoir.api.PouvoirAPI.placeholder
import com.skillw.pouvoir.api.annotation.AutoRegister
import com.skillw.pouvoir.api.function.action.ActionExecutor
import com.skillw.pouvoir.api.function.action.PouAction

/**
 * @className ActionMap
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. All rights reserved.
 */
@AutoRegister
object ActionString : PouAction<String>(String::class.java, "placeholder" to ActionExecutor { it.placeholder(parse()) })