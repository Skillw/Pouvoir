package com.skillw.asahi.internal.parser.prefix.type

import com.skillw.asahi.api.annotation.AsahiTypeParser
import com.skillw.asahi.api.lazyQuester
import com.skillw.asahi.api.member.quest.LazyQuester
import com.skillw.asahi.api.quest
import com.skillw.asahi.api.quester
import com.skillw.asahi.api.typeParser
import com.skillw.asahi.internal.util.Time

@AsahiTypeParser
private fun pair() = typeParser(Pair::class.java) {
    val first = quest<Any?>()
    this.expect("to", "=", ":")
    val second = quest<Any?>()
    quester { first.get() to second.get() }
}

@AsahiTypeParser
private fun time() = typeParser(Time::class.java) {
    val str = questString()
    quester {
        str.get().run {
            toLongOrNull()?.let { Time.tick(it) } ?: Time(this)
        }
    }
}

@AsahiTypeParser
private fun lazy() = typeParser(LazyQuester::class.java) {
    val script = parseScript()
    val lazy = lazyQuester { script.run() }
    quester { lazy }
}