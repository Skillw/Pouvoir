package com.skillw.pouvoir.api.annotation

@Target(AnnotationTarget.CLASS, AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class ScriptTopLevel(val key: String = "")
