package com.skillw.asahi.internal.namespacing.prefix.lang.util

import com.skillw.asahi.api.AsahiManager
import com.skillw.asahi.api.AsahiManager.topPrefixParsers
import com.skillw.asahi.api.AsahiManager.typeParsers
import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.prefixParser
import com.skillw.asahi.internal.util.AsahiClassBean
import com.skillw.pouvoir.util.script.MessageUtil.debug
import java.util.*

internal object PrefixDebug {

    private fun Collection<*>.debug() {
        val messages = LinkedList<String>()
        forEach {
            messages.add(it.toString())
        }
        messages.sort()
        messages.forEach(::debug)
    }

    @AsahiPrefix(["debug"], "lang")
    private fun debugFunc() = prefixParser {
        when (next()) {
            "context" -> result {
                debug(context())
            }

            "bean" ->
                when (val type = next()) {
                    "info" -> {
                        if (expect("of")) {
                            val any = questAny()
                            result {
                                AsahiClassBean.of(any.get()::class.java).info.debug()
                            }
                        } else
                            result {
                                AsahiClassBean.info().debug()
                            }
                    }

                    "load" -> {
                        val any = questAny()
                        result {
                            AsahiClassBean.of(any.get()::class.java)
                        }
                    }

                    else ->
                        error("Unknown function bean type $type")
                }

            "parsers" ->
                when (val parserType = next()) {
                    "token" -> result {
                        debug("Asahi Token Parsers: ")
                        topPrefixParsers.debug()
                    }

                    "type" -> result {
                        debug("Asahi Token Parsers: ")
                        typeParsers.values.debug()
                    }

                    else -> error("Unknown parser type $parserType")
                }

            "prefix" ->
                when (peek()) {
                    "native" -> result {
                        debug("Asahi Native Functions: ")
                        invokers.values.debug()
                    }

                    else -> result {
                        debug("Asahi Prefixes: ")
                        AsahiManager.namespaces.values.forEach {
                            debug("Namespace ${it.key}")
                            it.prefixMap.values.debug()
                        }
                    }
                }

            "namespaces" -> when (peek()) {
                "current" -> result {
                    debug("Current namespaces: ${namespaceNames().toList()}")
                }

                "all" -> result {
                    AsahiManager.namespaces.values.debug()
                }

                else -> result {
                    debug("Asahi Functions: ")
                    AsahiManager.namespaces.values.forEach {
                        debug("Namespace ${it.key}")
                        it.prefixMap.values.debug()
                    }
                }
            }

            "infix" ->
                result {
                    debug("Asahi Infix: ")
                    AsahiManager.namespaces.values.forEach {
                        debug("Namespace ${it.key}")
                        it.infixMap.values.debug()
                    }
                }

            "allActions" ->
                result {
                    debug("Asahi All Actions: ")
                    AsahiManager.namespaces.values.forEach {
                        debug("Namespace ${it.key}")
                        it.allInfixTokens.debug()
                    }
                }


            "info" ->
                when (val infoType = next()) {
                    "compile" -> {
                        info("Asahi Compile Info")
                        result { null }
                    }

                    "eval" -> result {
                        info("Asahi Eval Info")
                    }

                    else -> error("Unknown info type $infoType")
                }

            "on" ->
                when (val type = next()) {
                    "compile" -> {
                        debugOn()
                        result { null }
                    }

                    "eval" -> result {
                        debugOn()
                    }

                    else -> error("Unknown debug type $type")
                }

            "off" ->
                when (val type = next()) {
                    "compile" -> {
                        debugOff()
                        result { null }
                    }

                    "eval" -> result {
                        debugOff()
                        null
                    }

                    else -> error("Unknown debug type $type")
                }

            else -> {
                previous()
                val str = questString()
                result {
                    debug(str.get())
                }
            }
        }
    }

}