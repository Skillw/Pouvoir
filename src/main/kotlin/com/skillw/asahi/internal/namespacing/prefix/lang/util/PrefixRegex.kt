package com.skillw.asahi.internal.namespacing.prefix.lang.util

import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.prefixParser
import com.skillw.asahi.api.quest
import com.skillw.asahi.api.quester
import com.skillw.asahi.api.script.linking.NativeFunction

internal object PrefixRegex {
    @AsahiPrefix(["regexOf"], "regex")
    private fun regexOf() = prefixParser {
        val regex = quest<String>()
        val options = if (expect("with")) quest() else quester { emptySet<RegexOption>() }
        result { regex.get().toRegex(options.get()) }
    }

    @AsahiPrefix(["regex"], "regex")
    private fun regex() = prefixParser {
        val regex = if (expect("of")) quest<String>().quester { it.toRegex() } else quester { selector() }
        when (val type = next()) {
            "find" -> {
                val input = quest<String>()
                val index = if (expect("at")) quest() else quester { 0 }
                result {
                    regex.get().find(input.get(), index.get())
                }
            }

            "findAll" -> {
                val input = quest<String>()
                val index = if (expect("at")) quest() else quester { 0 }
                result {
                    regex.get().find(input.get(), index.get())
                }
            }

            "matches" -> {
                val input = quest<String>()
                val index = if (expect("at")) quest() else quester { 0 }
                result {
                    regex.get().matchesAt(input.get(), index.get())
                }
            }

            "replace" -> {
                val input = quest<String>()
                val replacement = if (expect("with")) quest<NativeFunction>() else quest<String>()
                result {
                    when (val obj = replacement.get()) {
                        is String -> regex.get().replace(input.get(), obj)
                        is NativeFunction -> {
                            regex.get().replace(input.get()) {
                                obj.invoke(this, it).toString()
                            }
                        }

                        else -> null
                    }
                }
            }

            else -> error("Unknown regex operate type $type")
        }
    }


}