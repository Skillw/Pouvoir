package com.skillw.pouvoir.internal.core.asahi.prefix.database

import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.prefixParser
import com.skillw.asahi.api.quest
import com.skillw.asahi.api.quester
import com.skillw.pouvoir.Pouvoir.databaseManager
import com.skillw.pouvoir.api.feature.database.ContainerHolder
import com.skillw.pouvoir.api.feature.database.UserBased
import com.skillw.pouvoir.api.plugin.map.DataMap
import com.skillw.pouvoir.internal.feature.database.PouvoirContainer


@AsahiPrefix(["container"], "lang")
private fun container() = prefixParser<Any?> {
    when (val top = next()) {
        "pouvoir" -> result {
            PouvoirContainer.container
        }

        "holder" -> {
            if (expect("pouvoir")) {
                return@prefixParser result {
                    PouvoirContainer.holder
                }
            }
            val type = if (expect("in")) questString() else quester { null }
            val mapQuester = questMap()
            result {
                val data = DataMap().apply { putAll(mapQuester.get().entries.associate { it.key to it.value!! }) }
                type.get()?.let {
                    databaseManager[it]?.connectWith(data)
                } ?: databaseManager.containerHolder(data)
            }
        }

        "create" -> {
            val key = questString()
            val user = !expect("normal")
            val holderGetter = if (expect("in")) quest<ContainerHolder<*>>() else quester { selector() }
            result {
                holderGetter.get().container(key.get(), user)
            }
        }

        else -> error("Unknown container operate type $top")
    }
}


@AsahiPrefix(["userdata"], "lang")
private fun userdata() = prefixParser<Any?> {
    val container = if (expect("of")) quest<UserBased>() else quester { selector() }
    val user = questString()
    when (val top = next()) {
        "set" -> {
            val key = questString()
            expect("=", "to")
            val value = questString()
            result {
                container.get()[user.get(), key.get()] = value.get()
            }
        }

        "get" -> {
            val key = questString()
            result {
                container.get()[user.get(), key.get()]
            }
        }

        "contains" -> {
            val key = questString()
            result {
                container.get().contains(user.get(), key.get())
            }
        }

        "delete" -> {
            val key = questString()
            result {
                container.get().delete(user.get(), key.get())
            }
        }


        else -> error("Unknown User Data operate type $top")
    }
}