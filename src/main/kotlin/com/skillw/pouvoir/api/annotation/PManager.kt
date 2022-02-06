package com.skillw.pouvoir.api.annotation

@Target(AnnotationTarget.FIELD)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class PManager(val impl: String = "undefined", vararg val params: String = [])
