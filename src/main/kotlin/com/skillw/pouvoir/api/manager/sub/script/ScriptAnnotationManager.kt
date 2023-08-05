package com.skillw.pouvoir.api.manager.sub.script

import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.plugin.map.KeyMap
import com.skillw.pouvoir.api.script.annotation.ScriptAnnotation

/** 脚本注解管理器 主要负责维护脚本注解 */
abstract class ScriptAnnotationManager : Manager, KeyMap<String, ScriptAnnotation>()