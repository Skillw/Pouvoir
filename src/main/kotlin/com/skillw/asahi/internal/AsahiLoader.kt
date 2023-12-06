package com.skillw.asahi.internal

import com.skillw.asahi.api.annotation.*
import com.skillw.asahi.api.member.AsahiRegistrable
import com.skillw.asahi.api.member.context.AsahiContext
import com.skillw.asahi.api.member.namespace.Namespace
import com.skillw.asahi.api.member.parser.infix.namespacing.BaseInfix
import com.skillw.asahi.api.member.parser.prefix.TopPrefixParser
import com.skillw.asahi.api.member.parser.prefix.namespacing.BaseJavaPrefix
import com.skillw.asahi.api.member.parser.prefix.namespacing.BasePrefix
import com.skillw.asahi.api.member.parser.prefix.namespacing.PrefixCreator
import com.skillw.asahi.api.member.parser.prefix.type.TypeParser
import com.skillw.pouvoir.util.instance
import com.skillw.pouvoir.util.plugin.PluginUtils.getClasses
import com.skillw.pouvoir.util.safe
import taboolib.common.platform.function.console
import taboolib.library.reflex.ClassAnnotation
import taboolib.library.reflex.ClassStructure
import taboolib.library.reflex.ReflexClass

object AsahiLoader {
    fun inject(clazz: ClassStructure) {
        safe {
            with(clazz) {
                register<AsahiNamespace, Namespace>()
                register<AsahiTopParser, TopPrefixParser<*>>()
                register<AsahiTypeParser, TypeParser<*>>()
                register<AsahiInfix, BaseInfix<*>>()
                register<AsahiGetter, AsahiContext.Getter>()
                register<AsahiSetter, AsahiContext.Setter>()
                registerFunctions()
            }
        }
    }

    fun init(vararg paths: String) {
        paths.forEach { path ->
            getClasses(path).map { ReflexClass.of(it).structure }.forEach(::inject)
        }
    }

    /**
     * FIXME:
     *  Author: Zhaoch23
     *  There has been some problem with the ClassAnnotation properties method that this function can parse
     *  some AsahiPrefix literals (i.e. money, sin, cos) but for some others (i.e. tell, actionbar) it throws the following.
     *  exception:
     *  java.lang.ClassCastException: class java.util.ArrayList cannot be cast to class
     *      [Ljava.lang.String; (java.util.ArrayList and [Ljava.lang.String; are in module java.base of loader 'bootstrap')
     *  I don't quite understand why this happened just for partial literals but I was managed to figure out a workaround.
     *  This may not solve the issue from the root but it works for my case.
     *  ( This may due to the type interpretations of the 'names' literal )
     */
    @Suppress("UNCHECKED_CAST")
    private fun <R> PrefixCreator<R>.register(asahi: ClassAnnotation, defaultName: String = "") {
        val properties: Map<String, Any> = asahi.properties()

        val namesUnchecked = properties["names"]
        val names: Array<String> = when (namesUnchecked) {
            is Array<*> -> namesUnchecked as? Array<String>
            is ArrayList<*> -> namesUnchecked.filterIsInstance<String>().toTypedArray()
            else -> null
        } ?: arrayOf(defaultName) // TODO: Some names weren't set ??
        val namespace: String = (properties["namespace"] as? String) ?: "null" // TODO: Some namespaces weren't set??
        val key = names.firstOrNull() ?: defaultName
        register(key, *names, namespace = namespace)
    }

    private fun ClassStructure.registerFunctions() {
        val obj = owner.instance
        if (obj is BasePrefix<*> && isAnnotationPresent(AsahiPrefix::class.java))
            obj.register()
        else if (obj is BaseJavaPrefix<*> && isAnnotationPresent(AsahiPrefix::class.java))
            obj.register(getAnnotation(AsahiPrefix::class.java), "none")
        else
            methods.forEach inner@{ method ->
                safe {
                    if (!method.isAnnotationPresent(AsahiPrefix::class.java) || method.returnType.simpleName != "PrefixCreator") return@safe
                    val creator =
                        (if (method.isStatic) method.invokeStatic() else method.invoke(obj!!)) as PrefixCreator<*>
                    creator.register(method.getAnnotation(AsahiPrefix::class.java), method.name)
                }
            }

    }

    private inline fun <reified A : Annotation, reified R : AsahiRegistrable<*>> ClassStructure.register() {
        val obj = owner.instance
        if (obj is R && isAnnotationPresent(A::class.java)) {
            obj.register()
        } else
            methods.forEach inner@{ method ->
                safe {
                    if (!method.isAnnotationPresent(A::class.java) || !R::class.java.isAssignableFrom(method.returnType)) return@safe
                    val parser = if (method.isStatic) method.invokeStatic() else method.invoke(obj!!) as R
                    (parser as? R?)?.register()
                }
            }
    }

}
