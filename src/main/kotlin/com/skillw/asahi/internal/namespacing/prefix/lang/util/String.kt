package com.skillw.asahi.internal.namespacing.prefix.lang.util

import com.skillw.asahi.api.AsahiAPI.analysis
import com.skillw.asahi.api.annotation.AsahiPrefix
import com.skillw.asahi.api.prefixParser
import com.skillw.asahi.api.quest

/**
 * @className String
 *
 * @author Glom
 * @date 2023/1/17 15:37 Copyright 2023 user. All rights reserved.
 */
@AsahiPrefix(["analysis", "inline"], "lang")
private fun inline() = prefixParser {
    val text = quest<String>()
    result { text.get().analysis(this, *namespaceNames()) }
}


