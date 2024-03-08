package com.skillw.asahi.internal.lexer

import com.skillw.asahi.api.member.lexer.AsahiDemand
import com.skillw.asahi.api.member.lexer.AsahiLexer
import com.skillw.asahi.internal.parser.prefix.top.StringParser
import com.skillw.pouvoir.util.putEntity
import org.bukkit.entity.Entity

/**
 * @className PouDemand
 *
 * @author Glom
 * @date 2023/1/14 12:38 Copyright 2024 Glom.
 */
internal class AsahiDemandImpl private constructor(entity: Entity? = null) : AsahiDemand(entity) {
    override val args = ArrayList<String>()
    override val tags = LinkedHashSet<String>()

    init {
        putEntity(entity)
    }

    private constructor(string: String) : this() {
        fun String.addTag() {
            tags.add(StringParser.content(substring(2)))
        }

        fun String.addParam(param: String) {
            put(substring(1), StringParser.content(param))
        }

        fun String.addArgs() {
            args.add(StringParser.content(this))
        }
        AsahiLexer.of(string).run {
            withEach { _ ->
                when {
                    startsWith("--") -> addTag()
                    startsWith("-") -> addParam(next())
                    else -> addArgs()
                }
            }
        }
    }

    override fun tag(tag: String): Boolean {
        return tags.contains(tag)
    }

    override fun tagAnyOf(vararg tags: String): Boolean {
        return tags.any { tag(it) }
    }

    override fun tagAllOf(vararg tags: String): Boolean {
        return tags.all { tag(it) }
    }


    companion object {
        fun of(string: String): AsahiDemand {
            return AsahiDemandImpl(string)
        }
    }
}