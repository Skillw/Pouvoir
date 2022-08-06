package com.skillw.pouvoir.api.annotation

/**
 * Auto register
 *
 * 自动注册至对应的RegContainer
 *
 * @constructor test 检测类名，如果存在则自动注册
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class AutoRegister(val test:String = "")
