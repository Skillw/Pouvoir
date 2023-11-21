package com.skillw.pouvoir.internal.core.asahi.prefix.bukkit

import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.member.lexer.AsahiLexer
import com.skillw.asahi.api.member.quest.Quester
import com.skillw.asahi.api.prefixParser
import com.skillw.asahi.api.quest
import com.skillw.asahi.api.quester
import com.skillw.asahi.api.script.linking.NativeFunction
import com.skillw.asahi.internal.util.Time
import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.feature.selector.*
import com.skillw.pouvoir.api.feature.selector.Target
import org.bukkit.Location
import org.bukkit.entity.Entity
import taboolib.common5.cbool

/**
 * @className Select
 *
 * @author Glom
 * @date 2023/1/14 0:57 Copyright 2023 user. All rights reserved.
 */

fun AsahiLexer.questTarget(): Quester<Target> {
    val getter = questObj()
    return quester {
        when (val obj = getter.get()) {
            is Location -> obj.toTarget()
            is Entity -> obj.toTarget()
            else -> error("Parse Target Failed!")
        }
    }
}


@AsahiPrefix(["targets"])
private fun targets() = prefixParser<Any?> {
    val result = quester { selectorSafely<SelectResult>() ?: select(SelectResult()) }
    when {
        expect("add") -> {
            val target = questTarget()
            result {
                result.get().add(target.get())
            }
        }

        expect("remove") -> {
            val target = questTarget()
            result {
                result.get().remove(target.get())
            }
        }

        expect("filter") -> {
            val todo = quest<NativeFunction>()
            result {
                val func = todo.get()
                result.get().filter {
                    func.invoke(this, it).cbool
                }
            }
        }

        expect("each", "every") -> {
            val todo = quest<NativeFunction>()
            result {
                val func = todo.get()
                result.get().forEach {
                    func.invoke(this, it)
                }
            }
        }

        expect("eachEntity", "everyEntity") -> {
            val todo = quest<NativeFunction>()
            result {
                val func = todo.get()
                result.get().forEachEntity {
                    func.invoke(this@result, this, it)
                }
            }
        }

        expect("eachLiving", "everyLiving") -> {
            val todo = quest<NativeFunction>()
            result {
                val func = todo.get()
                result.get().forEachLivingEntity {
                    func.invoke(this@result, this, it)
                }
            }
        }

        expect("eachLoc", "everyLoc") -> {
            val todo = quest<NativeFunction>()
            result {
                val func = todo.get()
                result.get().forEachLocation {
                    func.invoke(this@result, this, it)
                }
            }
        }

        else -> {
            val list = questList()
            result {
                return@result list.get().map { it as? SelectResult? }.lastOrNull()
            }
        }
    }
}

private fun AsahiLexer.params(): Any {
    return if (peek() == "{") {
        val map = HashMap<String, Quester<Any>>()
        expect("[", "{")
        do {
            val key = next()
            expect("to", "=", ":")
            val value = quest<Any>()
            map += key to value
            expect(",")
        } while (!expect("]", "}"))
        map
    } else {
        val params = ArrayList<Quester<Any>>()
        while (hasNext()
            && peek()?.startsWith("@") != true
            && peek() != "]"
        )
            params.add(quest())
        params
    }
}

@AsahiPrefix(["selector"])
private fun selector() = prefixParser<SelectResult> {
    val casterQuester = questTarget()
    expect("[")
    var token = next()
    val selectors = HashMap<BaseSelector, Any>()
    while (token.startsWith("@")) {
        val selector =
            Pouvoir.selectorManager[token.substring(1)] ?: error("Unknown Selector ${token.substring(1)}")
        selectors[selector] = params()
        if (expect("]")) break
        if (!hasNext()) break
        token = next()
    }
    result {
        val caster = casterQuester.get()
        val result = selectorSafely<SelectResult>() ?: select(SelectResult())
        selectors.forEach { (selector, paramsQuester) ->
            when (paramsQuester) {
                is List<*> -> {
                    val params = (paramsQuester as List<Quester<Any>>).map { it.get() }.toTypedArray()
                    selector.select(result, caster, *params)
                }

                is Map<*, *> -> {
                    val params = (paramsQuester as Map<String, Quester<Any>>).mapValues { it.value.get() }
                    selector.select(result, caster, params)
                }
            }
        }
        result
    }
}

@AsahiPrefix(["filter"])
private fun filter() = prefixParser<SelectResult> {
    val casterQuester = questTarget()
    expect("[")
    var token = next()
    val selectors = HashMap<BaseSelector, Pair<Any, Boolean>>()
    while (token.startsWith("@")) {
        var except = false
        val key = token.substring(1).run {
            if (first() == '!') {
                except = true
                substring(1)
            } else this
        }
        val selector =
            Pouvoir.selectorManager[key] ?: error("Unknown Selector $key")
        selectors[selector] = params() to except
        if (expect("]")) break
        if (!hasNext()) break
        token = next()
    }
    result {
        val caster = casterQuester.get()
        val result = selectorSafely<SelectResult>() ?: select(SelectResult())
        selectors.forEach { (selector, pair) ->
            val (paramsQuester, except) = pair
            when (paramsQuester) {
                is List<*> -> {
                    val params = (paramsQuester as List<Quester<Any>>).map { it.get() }.toTypedArray()
                    if (except)
                        selector.except(result, caster, *params)
                    else
                        selector.filter(result, caster, *params)
                }

                is Map<*, *> -> {
                    val params = (paramsQuester as Map<String, Quester<Any>>).mapValues { it.value.get() }
                    if (except)
                        selector.except(result, caster, params)
                    else
                        selector.filter(result, caster, params)
                }
            }
        }
        result
    }
}

private enum class OperateType {
    ADD, REMOVE, GET
}

@AsahiPrefix(["flag"])
private fun flag() = prefixParser<Any> {
    val operateType = quest<OperateType>()
    val key = quest<String>()
    val time = if (expect("time", "duration")) quest() else quester { Time.noTime }
    val entity = if (expect("to")) quest<Entity>() else quester { selector() }
    result {
        val flagKey = key.get()
        when (operateType.get()) {
            OperateType.ADD -> entity.get().addFlag(flagKey, time.get().toTick())
            OperateType.REMOVE -> entity.get().removeFlag(flagKey)
            OperateType.GET -> EntityFlag.getEntities(flagKey)
        }
    }
}