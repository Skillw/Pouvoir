package com.skillw.pouvoir.internal.core.asahi.infix.bukkit

import com.skillw.asahi.api.annotation.AsahiInfix
import com.skillw.asahi.api.member.parser.infix.namespacing.BaseInfix
import org.bukkit.boss.BossBar

/**
 * @className ActionCancellable
 *
 * @author Glom
 * @date 2022/8/9 16:26 Copyright 2022 user. 
 */
@AsahiInfix
object InfixBossBar : BaseInfix<BossBar>(BossBar::class.java, "bukkit") {
    init {
        "title" to { bar ->
            if (expect("to")) {
                bar.title = parse()
            }
            bar.title
        }
        "color" to { bar ->
            if (expect("to")) {
                bar.color = parse()
            }
            bar.color
        }
        "style" to { bar ->
            if (expect("to")) {
                bar.style = parse()
            }
            bar.style
        }
        "progress" to { bar ->
            if (expect("to")) {
                bar.progress = parse()
            }
            bar.progress
        }
        "visible" to { bar ->
            if (expect("to")) {
                bar.isVisible = parse()
            }
            bar.isVisible
        }
        "flag" to { bar ->
            when (next()) {
                "add" -> {
                    bar.addFlag(parse())
                }

                "remove" -> {
                    bar.removeFlag(parse())
                }

                "has" -> {
                    bar.hasFlag(parse())
                }

                else -> {}
            }
        }
        "player" to { bar ->
            when (next()) {
                "add" -> {
                    bar.addPlayer(parse())
                }

                "remove" -> {
                    bar.removePlayer(parse())
                }

                "removeAll" -> {
                    bar.removeAll()
                }

                else -> {}
            }
        }
    }
}