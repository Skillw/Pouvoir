package com.skillw.pouvoir.util.read

import java.util.regex.Pattern

/**
 * @className RegexUtil
 *
 * @author Glom
 * @date 2023/8/8 20:22 Copyright 2024 Glom. 
 */

val groupPattern = "\\(\\?<([a-zA-Z][a-zA-Z0-9]*)>".toRegex()

fun Pattern.groupNames(): Set<String> = groupPattern.findAll(pattern()).map { it.groupValues[1] }.toSet()