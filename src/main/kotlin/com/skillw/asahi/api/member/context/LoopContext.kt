package com.skillw.asahi.api.member.context

interface LoopContext {
    val label: String
    var isBreak: Boolean
    var isContinue: Boolean
    val parent: LoopContext?
    val subLoops: HashSet<LoopContext>
    fun searchLabel(label: String): LoopContext

    enum class Result {
        BREAK, CONTINUE
    }
}