package com.skillw.pouvoir.api.annotation

@MustBeDocumented
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class PManager(val impl: String = "undefined", vararg val params: String) {
}
