package com.skillw.asahi.api.member.quest

/**
 * @className LazyQuester
 *
 * 懒人式对象获取器
 *
 * 将此对象的变量放到上下文中后，解释变量后会返回执行内容
 *
 * @author Glom
 * @date 2023/1/14 11:47 Copyright 2023 user. All rights reserved.
 */
fun interface LazyQuester<R> : Quester<R>