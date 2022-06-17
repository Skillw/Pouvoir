package com.skillw.pouvoir.util


data class Pair<K, V>(val key: K, val value: V)

infix fun <A, B> A.to(that: B): Pair<A, B> = Pair(this, that)