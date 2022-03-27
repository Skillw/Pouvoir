package com.skillw.pouvoir.util


class Pair<K, V>(val key: K, val value: V) {
}

public infix fun <A, B> A.to(that: B): Pair<A, B> = Pair(this, that)