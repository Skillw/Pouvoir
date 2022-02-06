package com.skillw.pouvoir.api.annotation

@Target(AnnotationTarget.CLASS)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class Configurable(val config: String, val path: String)
