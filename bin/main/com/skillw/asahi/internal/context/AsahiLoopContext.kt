package com.skillw.asahi.internal.context

import com.skillw.asahi.api.member.context.AsahiContext
import com.skillw.asahi.api.member.context.LoopContext


/**
 * @className AsahiContextImpl
 *
 * @author Glom
 * @date 2022/12/24 15:05 Copyright 2022 user. All rights reserved.
 */
class AsahiLoopContext(
    override val label: String,
    override val parent: LoopContext? = null,
    basic: AsahiContext,
) : AsahiContext by basic, LoopContext {
    override var isBreak = false
    override var isContinue = false
    override val subLoops = HashSet<LoopContext>()
    override fun searchLabel(label: String): LoopContext {
        if (this.label == label) return this
        if (parent?.label == label) return parent
        return subLoops.firstOrNull { it.label == label } ?: error("No such Loop with the label $label")
    }

    override fun clone(): AsahiContext {
        return AsahiLoopContext(label, parent, clone())
    }


    companion object {
        fun AsahiContext.loopContext(label: String): AsahiLoopContext {
            val parent = if (this is LoopContext) this else null
            return AsahiLoopContext(label, parent, this)
        }
    }
}