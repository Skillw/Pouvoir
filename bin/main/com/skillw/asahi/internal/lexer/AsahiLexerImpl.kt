package com.skillw.asahi.internal.lexer

import com.skillw.asahi.api.AsahiAPI.compile
import com.skillw.asahi.api.AsahiManager
import com.skillw.asahi.api.member.lexer.AsahiLexer
import com.skillw.asahi.api.member.lexer.tokenizer.ScriptTokenizer
import com.skillw.asahi.api.member.namespace.NamespaceContainer
import com.skillw.asahi.api.member.quest.Quester
import com.skillw.asahi.api.questSafely
import com.skillw.asahi.api.quester
import com.skillw.asahi.api.script.AsahiCompiledScript
import com.skillw.asahi.api.script.AsahiQuestException
import com.skillw.asahi.util.condition.calcCondition
import com.skillw.pouvoir.util.condition.ConditionOperator.Companion.isConditionOperator
import com.skillw.pouvoir.util.condition.ConditionOperator.Companion.toConditionOperator
import com.skillw.pouvoir.util.script.MessageUtil
import java.util.*


/**
 * AsahiLexerImpl
 *
 * @constructor
 */
internal class AsahiLexerImpl : AsahiLexer {
    private var index = -1
    override val namespaces = NamespaceContainer()

    //组合优先于继承
    private val tokenizer: ScriptTokenizer
    private val tokens
        get() = tokenizer.tokens()

    private constructor(origin: String) {
        tokenizer = ScriptTokenizer.of(AsahiManager.replace(origin))
    }

    private constructor(lexer: AsahiLexerImpl) {
        tokenizer = lexer.tokenizer
    }

    private constructor(tokens: Collection<String>) {
        tokenizer = ScriptTokenizer.of(tokens)
    }

    companion object {
        
        fun of(script: String): AsahiLexerImpl {
            return AsahiLexerImpl(script)
        }

        
        fun of(tokens: Collection<String>): AsahiLexerImpl {
            return AsahiLexerImpl(tokens)
        }
    }

    /**
     * Except
     *
     * @param expects
     * @return
     */
    override fun expect(vararg expects: String): Boolean {
        return expects.any {
            (peekNextIgnoreBlank() == it).also { bool ->
                if (bool) {
                    next()
                    return@any true
                }
            }
        }
    }


    /**
     * Has next
     *
     * @return
     */
    override fun hasNext(): Boolean {
        val last = peek() == "\n" && index + 1 == tokens.lastIndex
        return index + 1 < tokens.size && !last
    }

    override fun current(): String {
        return tokens[index]
    }

    /**
     * Next
     *
     * @return
     */
    override fun next(): String {
        return tokens.getOrNull(++index).run { if (this == "\n") next() else this } ?: kotlin.run { index--;null }
        ?: error("Has no next")
    }

    override fun previous(): String? {
        if (index - 1 <= 0) {
            return null
        }
        return tokens[--index]
    }

    override fun currentIndex(): Int {
        return index
    }

    override fun peekNextIgnoreBlank(): String? {
        return tokens.getOrNull(index + 1)?.run {
            ifBlank {
                index++
                return peekNextIgnoreBlank().also { index-- }
            }
        }
    }

    override fun peek(): String? {
        return tokens.getOrNull(index + 1)
    }

    override fun skipTill(from: String, till: String): Boolean {
        var countIf = 0
        while (hasNext()) {
            when (next()) {
                from -> countIf++
                till -> if (--countIf <= 0) return true
                else -> {}
            }
        }
        return false
    }

    private fun trulyNext(): String = tokens[++index]

    override fun splitTill(from: String, to: String): ArrayList<String> {
        var count = if (tokens.getOrNull(currentIndex()) == "{") 1 else 0
        val tokens = ArrayList<String>()
        while (hasNext()) {
            when (val next = trulyNext()) {
                from ->
                    if (count++ == 0) {
                        continue
                    } else tokens.add(next)

                to -> if (--count <= 0) return tokens else tokens.add(next)
                else -> tokens.add(next)
            }
        }
        return tokens
    }

    override fun splitBefore(vararg to: String): ArrayList<String> {
        val tokens = ArrayList<String>()
        while (hasNext()) {
            if (peek() in to) {
                return tokens
            }
            tokens.add(trulyNext())
        }
        return tokens
    }

    override fun splitBeforeString(vararg to: String): String {
        val builder = StringBuilder()
        while (hasNext()) {
            if (peek() in to) {
                return builder.toString()
            }
            builder.append(" ${trulyNext()}")
        }
        return builder.toString()
    }

    override fun reset() {
        index = -1
    }

    override fun info(message: String, index: Int): String {
        return tokenizer.info(message, index)
    }

    override fun withEach(receiver: String.(Int) -> Unit) {
        while (hasNext()) {
            next().run { receiver(currentIndex()) }
        }
    }

    override fun questCondition(
        vararg till: String,
        boolParser: AsahiLexer.() -> Quester<Boolean>,
    ): Quester<Boolean> {
        val conditions = LinkedList<Quester<*>>()
        while (hasNext()) {
            if (peek() in till) break
            if (peek()?.isConditionOperator() == true) {
                conditions.add(next().toConditionOperator().let { quester { it } })
                continue
            }
            conditions.add(boolParser())
        }
        return quester { conditions.map { it.get() }.calcCondition() }
    }

    override fun error(message: String): Nothing {
        throw AsahiQuestException(info(message))
    }

    private var debug = false

    override fun debugOn() {
        debug = true
    }

    override fun debugOff() {
        debug = false
    }

    private fun debug() {
        if (debug) {
            MessageUtil.debug(info("Compile Debug-${currentIndex()}"))
        }
    }

    override fun questAllTo(script: AsahiCompiledScript) {
        while (hasNext()) {
            debug()
            script.add(questSafely<Any?>())
        }
        debug()
        debugOff()
    }

    override fun parseScript(vararg namespaces: String): AsahiCompiledScript {
        return splitTill("{", "}").compile(*namespaces, *namespaceNames())
    }

}