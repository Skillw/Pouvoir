package com.skillw.pouvoir.api.function

import com.skillw.pouvoir.api.function.context.IContext

interface IBlock<T> : IFunction<T> {
    fun execute(context: IContext): T?
}