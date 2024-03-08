package com.skillw.pouvoir.api.feature.operation

/**
 * @className StringOperation
 *
 * @author Glom
 * @date 2022/8/9 11:01 Copyright 2022 user.
 */
abstract class StringOperation(override val key: String) : Operation<String> {
    override var release: Boolean = false
}