package com.skillw.pouvoir.api.annotation


/**
 * Script top level
 *
 * 脚本中调用多参数顶级函数，请加[]
 *
 * 例如 arrayOf([a,b,c])
 *
 * @constructor Create empty Script top level
 * @property key 注入脚本后的变量名
 * @property description 描述 (checkUsableVars)
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ScriptTopLevel(val key: String = "", val description: String = "")
