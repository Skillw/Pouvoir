package com.skillw.asahi.internal.namespacing.infix

import com.skillw.asahi.api.annotation.AsahiInfix
import com.skillw.asahi.api.member.parser.infix.namespacing.BaseInfix

@AsahiInfix
internal object InfixMatchResult : BaseInfix<MatchResult>(MatchResult::class.java, "regex") {
    init {
        infix("value") { result ->
            result.value
        }
        infix("groups") { result ->
            if (expect("of")) {
                result.groups[parseString()]?.value
            }
            if (expect("at")) {
                result.groups[parseInt()]?.value
            } else
                result.groupValues
        }
    }
}