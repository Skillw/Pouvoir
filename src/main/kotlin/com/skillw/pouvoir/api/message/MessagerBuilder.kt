package com.skillw.pouvoir.api.message

import com.skillw.pouvoir.Pouvoir.messagerBuilderManager
import com.skillw.pouvoir.api.able.Registrable

/**
 * @className MessagerBuilder
 *
 * @author Glom
 * @date 2022/8/1 4:32 Copyright 2022 user. All rights reserved.
 */
abstract class MessagerBuilder(override val key: String) : Registrable<String> {
    abstract fun build(data: MessageData): Messager
    override fun register() {
        messagerBuilderManager.register(this)
    }
}