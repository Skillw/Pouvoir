package com.skillw.pouvoir.api.annotation

@Target(AnnotationTarget.CLASS)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class Defaultable(val config: String = "config", val path: String)
/**
 * 使用此注解的类 必须有一个静态且名为 default 的变量
 */