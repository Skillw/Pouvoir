package com.skillw.asahi.api.member.lexer

import com.skillw.asahi.api.member.namespace.NamespaceHolder
import com.skillw.asahi.api.member.quest.LazyQuester
import com.skillw.asahi.api.member.quest.Quester
import com.skillw.asahi.api.quest
import com.skillw.asahi.api.questSafely
import com.skillw.asahi.api.questType
import com.skillw.asahi.api.quester
import com.skillw.asahi.api.script.AsahiCompiledScript
import com.skillw.asahi.internal.lexer.AsahiLexerImpl
import com.skillw.asahi.internal.util.Time

/**
 * AsahiLexer
 *
 * @constructor Create empty I asahi lexer
 */
interface AsahiLexer : NamespaceHolder<AsahiLexer> {

    /**
     * 预期下一个token
     *
     * 若 下一个token 预期正确 则 跳过下一个token 并返回true
     *
     * 否则 不跳过下一个token 并返回false
     *
     * @param expects
     * @return 下一个token是否符合预期
     */
    fun expect(vararg expects: String): Boolean

    /** @return 是否有下一个Token */
    fun hasNext(): Boolean

    /** @return 是否读取结束 */
    fun isEnd(): Boolean = !hasNext()

    /**
     * 当前Token
     *
     * @return 当前Token
     */
    fun current(): String

    /**
     * 下一个Token
     *
     * @return 下一个Token
     */
    fun next(): String

    /**
     * 上一个Token 无上一个Token时为null
     *
     * @return 上一个Token
     */
    fun previous(): String?

    /**
     * 当前Token下标
     *
     * @return 当前Token下标
     */
    fun currentIndex(): Int

    /**
     * 查看下一个Token(忽略空字符)
     *
     * @return 下一个Token(忽略空字符)
     */
    fun peekNextIgnoreBlank(): String?

    /**
     * 查看下一个Token(不忽略空字符)
     *
     * @return 下一个Token(不忽略空字符)
     */
    fun peek(): String?

    /**
     * 跳过一些Token
     *
     * @param from 开头Token
     * @param till 结尾Token
     * @return 是否跳过成功
     */
    fun skipTill(from: String, till: String): Boolean

    /**
     * 将一些Token 分割出来
     *
     * @param from 开头Token
     * @param to 结尾Token
     * @return 中间的Tokens
     */
    fun splitTill(from: String, to: String): List<String>

    /**
     * 将一些Token 分割出来
     *
     * @param to 结尾Token
     * @return to前的Tokens
     */
    fun splitBefore(vararg to: String): List<String>

    /**
     * 将一些Token 分割出来 并以' '做分隔符结合为字符串
     *
     * @param to 结尾Token
     * @return to前的Tokens以' '做分隔符结合为的字符串
     */
    fun splitBeforeString(vararg to: String): String

    /** 重置Reader，下标归0 */
    fun reset()

    /**
     * 当前信息（包括脚本，下标）
     *
     * @return 当前信息（包括脚本，下标）
     */
    fun info(message: String = "", index: Int = currentIndex()): String

    /**
     * 寻求字符串
     *
     * @return Quester<String>
     */
    fun questString() = quest<String>()

    /** 寻求 int */
    fun questInt() = quest<Int>()

    /** 寻求 double */
    fun questDouble() = quest<Double>()

    /** 寻求 float */
    fun questFloat() = quest<Float>()

    /** 寻求 byte */
    fun questByte() = quest<Byte>()

    /** 寻求 short */
    fun questShort() = quest<Short>()

    /** 寻求 boolean */
    fun questBoolean() = quest<Boolean>()

    /** 寻求 long */
    fun questLong() = quest<Long>()

    /** 寻求 list */
    fun questList() = quest<MutableList<Any?>>()

    /** 寻求 array */
    fun questArray() = quest<Array<Any?>>()

    /** 寻求 map */
    fun questMap() = quest<MutableMap<String, Any?>>()

    /** 寻求 tokenizer */
    fun questObj() = questSafely<Any?>()

    /** 寻求 tokenizer */
    fun questAny() = quest<Any>()

    /** 寻求 代码块 */
    fun questLazy() = quest<LazyQuester<Any?>>()

    /** 寻求 Time */
    fun questTime() = quest<Time>()

    /** 跳过下一个语句 */
    fun skip() = questSafely<Any?>()

    /** 寻求 Tick */
    fun questTick() = quest<Time>().quester { it.toTick() }

    /** 通过Map类型解释器寻求Map */
    fun questTypeMap() = questType<MutableMap<String, Any?>>()

    /** 通过List类型解释器寻求List */
    fun questTypeList() = questType<MutableList<Any?>>()

    /** 寻通过Array类型解释器寻求Array */
    fun questTypeArray() = questType<Array<Any?>>()

    /**
     * 将每个Token作为接收器
     *
     * @param receiver
     * @receiver
     */
    fun withEach(receiver: String.(Int) -> Unit)

    /**
     * 寻求条件表达式
     *
     * @param till 截止Token
     * @param boolQuester 自定义bool寻求函数
     * @return 条件表达式Quester
     * @receiver
     */
    fun questCondition(
        vararg till: String,
        boolQuester: AsahiLexer.() -> Quester<Boolean> = { questBoolean() },
    ): Quester<Boolean>

    /**
     * 报错，会打印当前读取位置与堆栈
     *
     * @param message 报错信息
     * @return
     */
    fun error(message: String): Nothing

    /** 开启编译时调试 */
    fun debugOn()

    /** 关闭编译时调试 */
    fun debugOff()

    /**
     * 将词法器中所有Quester都塞到脚本里
     *
     * @param script 脚本
     */
    fun questAllTo(script: AsahiCompiledScript)

    /**
     * 解析一段无参函数
     *
     * @param namespaces 命名空间
     * @return
     */
    fun parseScript(vararg namespaces: String): AsahiCompiledScript

    companion object {
        /**
         * 根据脚本生成词法器
         *
         * @param script String 脚本字符串
         * @return AsahiLexer 词法器
         */
        @JvmStatic
        fun of(script: String): AsahiLexer {
            return AsahiLexerImpl.of(script)
        }

        /**
         * 根据已分割好的Tokens 生成词法器
         *
         * @param tokens Collection<String> 已分割好的Tokens
         * @return AsahiLexer 词法器
         */
        @JvmStatic
        fun of(tokens: Collection<String>): AsahiLexer {
            return AsahiLexerImpl.of(tokens)
        }
    }
}