package com.skillw.pouvoir.api.annotation

@MustBeDocumented
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Defaultable(val config: String = "config", val path: String)
