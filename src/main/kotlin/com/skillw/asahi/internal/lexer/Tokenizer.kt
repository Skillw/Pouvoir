package com.skillw.asahi.internal.lexer

import com.skillw.asahi.api.member.lexer.tokenizer.Line
import com.skillw.asahi.api.member.lexer.tokenizer.ScriptTokenizer
import java.util.*

object Tokenizer {
    internal enum class StringType {
        /**
         * Single
         *
         * @constructor Create empty Single
         */
        SINGLE,

        /**
         * Multiple
         *
         * @constructor Create empty Multiple
         */
        MULTIPLE
    }

    @JvmStatic
    fun String.toTokens(modifier: (ArrayList<String>) -> Unit = {}): ArrayList<String> {
        return prepareTokens(this).first.also(modifier)
    }

    internal fun prepareTokens(
        text: String,
        modifier: (ArrayList<String>) -> Unit = {},
    ): Pair<ArrayList<String>, Map<IntRange, Line>> {
        val indexesToLine = HashMap<IntRange, Line>()
        val tokens = ArrayList<String>()
        val cache = StringBuilder()
        var string: StringType? = null
        var escape = false
        var notes = false
        var line = 1
        var lineStart = 0
        var lineEnd: Int
        fun push(char: Char) {
            cache.append(char)
        }

        fun addToken() {
            if (cache.isNotEmpty())
                tokens.add(cache.toString())
            cache.clear()
        }

        fun nextLine() {
            addToken()
            tokens.add("\n")
            lineEnd = tokens.size - 1
            val range = lineStart..lineEnd
            indexesToLine[range] = Line(line++, range)
            lineStart = tokens.size
        }

        text.forEach {
            if (notes) {
                //遇到换行符就退出注释模式
                if (it == '\n') {
                    line++
                    notes = false
                }
                //如果在注释中，就跳过
                return@forEach
            }
            when (it) {
                //单行注释
                '#' -> {
                    if (string != null) {
                        push(it)
                        return@forEach
                    }
                    notes = true
                }

                //转义
                '\\' -> {
                    if (escape) {
                        push(it)
                        escape = false
                        return@forEach
                    }
                    escape = true
                }

                '\'' -> {
                    push(it)
                    if (escape) {
                        escape = false
                        return@forEach
                    }
                    when (string) {
                        StringType.MULTIPLE -> {}
                        StringType.SINGLE -> string = null
                        null -> string = StringType.SINGLE
                    }

                }

                //字符串
                '"' -> {
                    push(it)
                    if (escape) {
                        escape = false
                        return@forEach
                    }
                    when (string) {
                        StringType.MULTIPLE -> string = null
                        StringType.SINGLE -> {}
                        null -> string = StringType.MULTIPLE
                    }
                }

                //换行
                '\n' -> {
                    if (escape) {
                        push('\\')
                        push('n')
                        escape = false
                        return@forEach
                    }
                    if (string != null) {
                        push(it)
                        return@forEach
                    }
                    nextLine()
                }

                //空格
                ' ' -> {
                    if (string != null) {
                        push(it)
                        return@forEach
                    }
                    addToken()
                }

                ';' -> {
                    if (string != null) {
                        push(it)
                        return@forEach
                    }
                    addToken()
                    push(';')
                    addToken()
                }

                //常规字符
                else -> {
                    push(it)
                }
            }
        }
        if (cache.isNotEmpty() && cache.isNotBlank()) {
            addToken()
            nextLine()
        }
        val result = ArrayList<String>(tokens.size)
        tokens.forEachIndexed { index, token ->
            if (token == "\n" && tokens.getOrNull(index - 1) == "\n") {
                return@forEachIndexed
            }
            result.add(token)
        }
        return result.also(modifier) to indexesToLine
    }

    internal fun prepareTokens(list: Collection<String>): Pair<ArrayList<String>, Map<IntRange, Line>> {
        val indexesToLine = HashMap<IntRange, Line>()
        val tokens = ArrayList<String>()
        var lineStart = 0
        var lineEnd: Int
        var line = 1

        fun nextLine() {
            lineEnd = tokens.size - 1
            val range = lineStart..lineEnd
            indexesToLine[range] = Line(line++, range)
            lineStart = tokens.size
        }

        list.forEach { token ->
            tokens.add(token)
            if (token == "\n") nextLine()
        }
        return tokens to indexesToLine
    }

    internal fun prepareInfo(message: String, index: Int, tokenizer: ScriptTokenizer): String {
        val entry = tokenizer.getLine(index)
        // 下标范围 to 行数
        val (line, range) = entry
        //处理原文本， 以换行符分割
        val list = LinkedList(tokenizer.script().split("\n"))
        val indexInLine = index - range.first
        val lastIndex = range.last - range.first
        val lineStr = list[line - 1]
        val fixedIndex = lineStr.run {
            if (indexInLine == lastIndex) lastIndex else {
                Regex(
                    prepareTokens(lineStr).first.subList(indexInLine, lastIndex)
                        .joinToString("\\s?", postfix = "$")
                        { Regex.escape(it) }
                ).find(lineStr)?.range?.first ?: return@run lastIndex
            }
        }

        list.add(
            line,
            lineStr.run {
                substring(0, fixedIndex)
                    .run {
                        " ".repeat(realLength()) + "↑"
                    }
            })
        return "$message at line $line , $indexInLine in \n${list.joinToString("\n")}"
    }

    /** 获取字符串的实际长度 坏黑提供的函数 */
    private fun String.realLength(): Int {
        val regex = "[\u3091-\uFFe5]".toRegex()
        return sumBy { if (it.toString().matches(regex)) 2 else 1 }
    }
}