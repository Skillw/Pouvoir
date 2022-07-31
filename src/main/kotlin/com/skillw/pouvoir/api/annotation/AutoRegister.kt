package com.skillw.pouvoir.api.annotation

/**
 * Auto register
 *
 * 自动注册至对应的RegContainer
 *
 * @constructor no params
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class AutoRegister
