package com.skillw.asahi.api.member.parser.infix

import com.skillw.asahi.api.member.lexer.AsahiLexer
import com.skillw.asahi.api.member.quest.Quester
import com.skillw.asahi.internal.parser.infix.ActionParserImpl


/**
 * InfixParser
 *
 * 语言层面的中缀解释器
 *
 * 负责从词法器中截取应该由中缀解释器解释的Token段
 *
 * 并交给对应类型的中缀解释器解释，并返回最终结果
 *
 * @constructor Create empty Infix parser
 */
abstract class InfixParser {

    companion object {
        @JvmStatic
        fun get(): InfixParser {
            return ActionParserImpl
        }
    }

    /** 忽略的token，当词法器读到的token在其中时则停止中缀解释器的解释 */
    abstract val ignores: HashSet<String>

    /**
     * 编译(parse)中缀动作
     *
     * @param getter 对象获取者
     * @return 中缀解释器解释后的对象获取者
     */
    protected abstract fun AsahiLexer.parse(getter: Quester<Any?>): Quester<Any?>

    /**
     * 编译(parse)中缀动作
     *
     * @param lexer 词法器
     * @param getter 对象获取者
     * @return 中缀解释器解释后的对象获取者
     */
    fun parseInfix(lexer: AsahiLexer, getter: Quester<*>): Quester<Any?> {
        return lexer.parse(getter as Quester<Any?>)
    }
}