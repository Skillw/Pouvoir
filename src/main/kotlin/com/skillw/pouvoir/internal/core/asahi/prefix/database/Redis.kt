package com.skillw.pouvoir.internal.core.asahi.prefix.database

import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.prefixParser
import com.skillw.asahi.api.quest
import com.skillw.asahi.api.quester
import com.skillw.pouvoir.internal.feature.database.redis.RedisContainer

/**
 * @className MongoDB
 *
 * @author Glom
 * @date 2023/1/19 15:06 Copyright 2023 user. All rights reserved.
 */
@AsahiPrefix(["redis"], "lang")
private fun redis() = prefixParser {
    val connectionGetter =
        if (expect("of")) quest<RedisContainer>().quester { it.connection }
        else quester { selector<RedisContainer>().connection }
    when (val top = next()) {
        "set" -> {
            val key = questString()
            expect("=", "to")
            val value = questString()
            result {
                connectionGetter.get()[key.get()] = value.get()
            }
        }

        "get" -> {
            val key = questString()
            result {
                connectionGetter.get()[key.get()]
            }
        }

        "contains" -> {
            val key = questString()
            result {
                connectionGetter.get().contains(key.get())
            }
        }

        "delete" -> {
            val key = questString()
            result {
                connectionGetter.get().delete(key.get())
            }
        }

        else -> error("Unknown Redis operate type $top")
    }
}