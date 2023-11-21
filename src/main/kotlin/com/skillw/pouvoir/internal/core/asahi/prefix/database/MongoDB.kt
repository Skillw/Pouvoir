package com.skillw.pouvoir.internal.core.asahi.prefix.database

import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.prefixParser
import com.skillw.asahi.api.quest
import com.skillw.asahi.api.quester
import com.skillw.pouvoir.internal.feature.database.mongodb.MongoContainer

/**
 * @className MongoDB
 *
 * @author Glom
 * @date 2023/1/19 15:06 Copyright 2023 user. All rights reserved.
 */
@AsahiPrefix(["mongo"])
private fun mongo() = prefixParser<Any> {
    val container =
        if (expect("of")) quest<MongoContainer>()
        else quester { selector() }
    when (val top = next()) {
        "insert" -> {
            val map = questMap()
            result {
                container.get().insert(map.get())
            }
        }

        "find" -> {
            val map = questMap()
            result {
                println(map.get())
                container.get().find(map.get())
            }
        }

        "contains" -> {
            val map = questMap()
            result {
                container.get().find(map.get()).firstOrNull() != null
            }
        }

        "delete" -> {
            val map = questMap()
            result {
                container.get().delete(map.get())
            }
        }

        else -> error("Unknown MongoDB operate type $top")
    }
}