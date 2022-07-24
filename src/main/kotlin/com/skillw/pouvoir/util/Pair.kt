package com.skillw.pouvoir.util

// Kotlin's Pair will be relocated....
// So this class is here.
data class Pair<K, V>(val key: K, val value: V)

infix fun <A, B> A.to(that: B): Pair<A, B> = Pair(this, that)