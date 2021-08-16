package com.skillw.pouvoir.api.annotation

@MustBeDocumented
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Configurable(val config: String, val path: String)
