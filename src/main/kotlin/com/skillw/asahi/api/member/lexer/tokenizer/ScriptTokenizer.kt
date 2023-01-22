package com.skillw.asahi.api.member.lexer.tokenizer

import com.skillw.asahi.internal.lexer.Tokenizer

/**
 * @className MultiLine
 *
 * @author Glom
 * @date 2023/1/19 16:28 Copyright 2023 user. All rights reserved.
 */
class ScriptTokenizer {
    private val tokens = ArrayList<String>()
    private val indexesToLine = HashMap<IntRange, Line>()
    private val scriptGen: () -> String

    private constructor(script: String) {
        this.scriptGen = { script }
        val (tokens, indexesToLine) = Tokenizer.prepareTokens(script)
        this.tokens.addAll(tokens)
        this.indexesToLine.putAll(indexesToLine)
    }

    private constructor(tokens: Collection<String>) {
        this.scriptGen = { tokens.joinToString(" ") }
        val (tokensTemp, indexesToLine) = Tokenizer.prepareTokens(tokens)
        this.tokens.addAll(tokensTemp)
        this.indexesToLine.putAll(indexesToLine)
    }

    fun tokens(): List<String> {
        return tokens
    }

    fun script(): String {
        return scriptGen()
    }

    fun getLine(index: Int): Line {
        return indexesToLine.filterKeys { index in it }.values.first()
    }

    fun info(message: String, index: Int): String {
        return Tokenizer.prepareInfo(message, index, this)
    }

    companion object {
        @JvmStatic
        fun of(origin: String): ScriptTokenizer {
            return ScriptTokenizer(origin)
        }

        @JvmStatic
        fun of(tokens: Collection<String>): ScriptTokenizer {
            return ScriptTokenizer(tokens)
        }
    }
}