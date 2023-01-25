package com.skillw.asahi.api

import com.skillw.asahi.api.member.context.AsahiContext
import com.skillw.asahi.api.member.namespace.Namespace
import com.skillw.asahi.api.member.namespace.NamespaceHolder
import com.skillw.asahi.api.member.parser.prefix.PrefixParser
import com.skillw.asahi.api.member.parser.prefix.TopPrefixParser
import com.skillw.asahi.api.member.parser.prefix.type.TypeParser
import com.skillw.asahi.api.member.quest.Quester
import com.skillw.asahi.internal.namespace.Common
import com.skillw.asahi.internal.namespace.Lang
import taboolib.common5.util.replace
import java.util.*

object AsahiManager {

    /** (顶级)语言解释器 Lang Parsers 解释器容器， id -> 解释器 */
    internal val topPrefixParsers = LinkedList<TopPrefixParser<*>>()

    /** (低级)类型解释器 Type Parsers 类型 解释器容器， 类型 -> 解释器 */
    internal val typeParsers = HashMap<Class<*>, TypeParser<*>>()

    /** Namespace 命名空间容器， id -> 命名空间(里面有中级解释器: 函数 , 后缀动作) */
    internal val namespaces = HashMap<String, Namespace>()

    /** 宏定义，直接替换原脚本 */
    private val macros = HashMap<String, String>()

    init {
        Common.register()
        Lang.register()
    }


    fun getNamespace(key: String): Namespace {
        return namespaces.computeIfAbsent(key) { Namespace(key) }
    }

    fun getNamespaces(vararg keys: String): Set<Namespace> {
        return keys.map { getNamespace(it) }.toSet()
    }


    fun registerParser(parser: TypeParser<*>) {
        val types = parser.key
        types.forEach { type ->
            typeParsers[type] = parser
        }
    }

    fun hasParser(type: Class<*>): Boolean {
        return typeParsers.containsKey(type)
    }

    fun <R> getParser(type: Class<R>): PrefixParser<R>? {
        return typeParsers[type] as? PrefixParser<R>?
    }

    @JvmStatic
    fun define(from: String, to: String) {
        macros[from] = to
    }

    internal fun replace(script: String): String = script.replace(*macros.map { it.key to it.value }.toTypedArray())

    fun loadSharedNamespace(holder: NamespaceHolder<*>) {
        namespaces
            .values
            .filter(Namespace::shared)
            .forEach(holder.namespaces::add)
    }

    fun <R> result(exec: AsahiContext.() -> R): Quester<R> {
        return Quester { exec() }
    }
}