package com.skillw.asahi.internal.parser.infix

import com.skillw.asahi.api.member.context.AsahiContext
import com.skillw.asahi.api.member.context.InfixContext
import com.skillw.asahi.api.member.lexer.AsahiLexer
import com.skillw.asahi.api.member.parser.infix.InfixParser
import com.skillw.asahi.api.member.parser.infix.namespacing.BaseInfix
import com.skillw.asahi.api.member.quest.Quester
import com.skillw.asahi.api.quester
import com.skillw.asahi.internal.parser.prefix.top.StringParser
import com.skillw.asahi.internal.parser.prefix.top.VarParser
import java.util.*

internal object ActionParserImpl : InfixParser() {
    override val ignores = HashSet<String>().apply {
        add("\n")
        add(";")
        add(".")
    }

    override fun AsahiLexer.parse(getter: Quester<Any?>): Quester<Any?> {
        //是否是变量调用
        if (peek()?.let { VarParser.isCallVar(it) || StringParser.isWrapping(it) } == true) return getter
        //查看下一个token 是否在"忽略列表"中
        if (peek() in ignores) {
            expect(";", ".")
            return getter
        }
        //查看下一个token 是否在 allActions里(所有后缀动作的set)
        if (namespaces.all { !it.hasInfix(peek()) }) return getter
        //以上3个判断，都是为了过滤掉非后缀动作

        val tokens = LinkedList<String>()
        while (hasNext() && peek() !in ignores) {
            val next = next()
            if (ignores.any { next.endsWith(it) }) {
                tokens += next.substring(0, next.lastIndex)
                break
            } else tokens += next
        }
        expect(";", ".")
        //后缀动作 参数
        val actionReader = AsahiLexer.of(tokens)
        //后缀动作 上下文
        val context = quester { InfixContext(context(), actionReader) }
        //执行内容 （Supplier）
        return infixQuester(getter, tokens) {
            //获取对象
            //这时候才知道对象的类型
            var obj = getter.get() ?: return@infixQuester null
            var baseInfix = infixOf(obj) as? BaseInfix<Any>? ?: return@infixQuester obj
            //获取上下文
            context.get().run {
                reset()
                while (hasNext()) {
                    if (this.token == "@NONE")
                        this.token = next()
                    //基于对象类型 获取BaseAction<T>对象 并调用动作
                    obj = baseInfix.run { infix(obj) } ?: return@run "null"
                    baseInfix = infixOf(obj) as? BaseInfix<Any>? ?: return@infixQuester obj
                    this.token = "@NONE"
                }
                obj
            }
        }
    }

    private fun AsahiLexer.infixOf(obj: Any): BaseInfix<*>? {
        val type = obj::class.java
        return namespaces.firstOrNull { it.hasInfix(type) }?.infixOf(obj)
    }

    private fun <R> infixQuester(
        quester: Quester<*>,
        actions: Collection<String>,
        quest: AsahiContext.() -> R,
    ): Quester<R> {
        return object : Quester<R> {
            override fun AsahiContext.execute(): R {
                return quest()
            }

            override fun toString(): String {
                return "Infix Quester - $quester - $actions"
            }
        }
    }
}