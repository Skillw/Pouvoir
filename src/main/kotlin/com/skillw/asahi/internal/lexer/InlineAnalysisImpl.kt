package com.skillw.asahi.internal.lexer

import com.skillw.asahi.api.AsahiAPI.compile
import com.skillw.asahi.api.AsahiManager
import com.skillw.asahi.api.InlineAnalysis
import com.skillw.asahi.api.member.context.AsahiContext
import com.skillw.asahi.api.member.namespace.Namespace
import com.skillw.asahi.api.member.quest.Quester
import com.skillw.asahi.api.quester
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * @className InlineReaderImpl
 *
 * @author Glom
 * @date 2022/12/27 9:12 Copyright 2022 user. All rights reserved.
 */
internal class InlineAnalysisImpl private constructor(val text: String) :
    InlineAnalysis {
    //          下标范围 -> Asahi段内容
    private val beingReplaced = LinkedList<Pair<IntRange, Quester<Any?>>>()

    override val namespaces: MutableSet<Namespace> = HashSet()


    init {
        AsahiManager.loadSharedNamespace(this)
    }

    companion object {
        private val cache = ConcurrentHashMap<Int, InlineAnalysis>()

        //字符是否符合命名规范
        private val charRegex = Regex("([^\\x00-\\xff]|[a-zA-Z0-9_$.])")

        @JvmStatic
        fun of(str: String): InlineAnalysis {
            return cache.computeIfAbsent(str.hashCode()) { InlineAnalysisImpl(str) }
        }
    }


    init {
        //是否在内联脚本中
        var inScript = false
        var begin = 0
        //下个字符是否转义
        var escape = false
        //缓存内联脚本字符串
        val cache = StringBuilder()
        //{ } 嵌套计数器
        var count = 0

        var inlineVar = false

        fun isEscape(): Boolean = if (escape) {
            escape = false
            true
        } else false

        //将字符加入缓存
        fun push(char: Char) {
            cache.append(char)
        }

        fun pull() {
            cache.deleteAt(cache.lastIndex)
        }

        //一段内联脚本读完，将内联脚本加入map
        fun finish(end: Int) {
            val script = cache.toString()
            beingReplaced.addFirst(begin..end to script.compile(*namespaceNames()))
            cache.clear()
            inScript = false
        }

        fun removeAt(index: Int) {
            beingReplaced.addFirst(index..index to quester { "" })
        }

        fun inlineVar(index: Int, char: Char) {
            if (char == '{') return
            when {
                char == '}' -> {
                    finish(index)
                    inlineVar = false
                }

                !charRegex.containsMatchIn(char.toString()) -> {
                    finish(index - 1)
                    inlineVar = false
                }

                else -> {
                    push(char)
                }
            }
        }



        text.forEachIndexed loop@{ index, char ->
            if (inlineVar) {
                inlineVar(index, char)
                return@loop
            }
            //如果在内联脚本里，则压入字符
            if (inScript) push(char)
            when (char) {
                //转义
                '\\' -> {
                    if (isEscape()) return@loop
                    //如果不转义，且在内联脚本里，就删除此次压入
                    if (!isEscape() && inScript) pull()
                    //下个字符转义
                    escape = true
                    //删除当前转义字符
                    removeAt(index)
                }
                //内联脚本段开始
                '{' -> {
                    when {
                        //如果转义直接跳过
                        isEscape() -> return@loop
                        //如果在内联脚本段 则嵌套计数器+1
                        inScript -> count++
                        //开始Asahi段
                        else -> {
                            begin = index
                            inScript = true
                        }
                    }
                }
                //内联脚本段结束
                '}' -> {
                    when {
                        //不在脚本 或 在转义则跳过
                        !inScript || isEscape() -> return@loop
                        //如果嵌套计数器不为0
                        count != 0 -> count--
                        //结束Asahi段
                        else -> {
                            pull()
                            finish(index)
                        }
                    }
                }

                '$' -> {
                    when {
                        //如果 在内联变量段 在内联脚本里 转义 则直接跳过
                        inlineVar || inScript || isEscape() -> return@loop
                        //开始内联变量段
                        else -> {
                            begin = index
                            inlineVar = true
                            push('$')
                        }
                    }

                }
            }
        }
    }

    override fun analysis(context: AsahiContext): String {
        var temp = text
        context.run {
            for ((range, script) in beingReplaced) {
                temp = temp.replaceRange(range, script.run().toString())
            }
        }
        return temp
    }
}