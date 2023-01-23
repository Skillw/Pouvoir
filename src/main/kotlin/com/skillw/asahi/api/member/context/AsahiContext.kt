package com.skillw.asahi.api.member.context

import com.skillw.asahi.api.member.AsahiRegistrable
import com.skillw.asahi.api.member.quest.Quester
import com.skillw.asahi.api.script.linking.InvokerHolder
import com.skillw.asahi.internal.context.AsahiContextImpl
import java.util.*
import java.util.concurrent.CompletableFuture
import javax.script.Bindings

/**
 * Asahi Context
 *
 * 脚本上下文
 *
 * @constructor Create empty Asahi context
 */
interface AsahiContext : MutableMap<String, Any>, Bindings, InvokerHolder {
    abstract class Getter(override val key: String, val priority: Int) : AsahiRegistrable<String>, Comparable<Getter> {
        protected abstract fun AsahiContext.filter(key: String): Boolean
        protected abstract fun AsahiContext.getValue(key: String): Any?

        fun filterKey(context: AsahiContext, key: String): Boolean {
            return context.filter(key)
        }

        fun get(context: AsahiContext, key: String): Any? {
            return context.getValue(key)
        }

        override fun register() {
            registerGetter(this)
        }

        override fun compareTo(other: Getter): Int = if (this.priority == other.priority) 0
        else if (this.priority > other.priority) 1
        else -1
    }

    abstract class Setter(override val key: String, val priority: Int) : AsahiRegistrable<String>, Comparable<Setter> {
        protected abstract fun AsahiContext.filter(key: String): Boolean
        protected abstract fun AsahiContext.setValue(key: String, value: Any): Any?

        fun filterKey(context: AsahiContext, key: String): Boolean {
            return context.filter(key)
        }

        fun set(context: AsahiContext, key: String, value: Any): Any? {
            return context.setValue(key, value)
        }

        override fun register() {
            registerSetter(this)
        }

        override fun compareTo(other: Setter): Int = if (this.priority == other.priority) 0
        else if (this.priority > other.priority) 1
        else -1
    }

    fun getOrigin(key: String): Any?
    fun setOrigin(key: String, value: Any): Any?

    /**
     * Put deep
     *
     * @param key 键
     * @param value 值
     * @return
     */
    fun putDeep(key: String, value: Any): Any?

    /**
     * Get deep
     *
     * @param key 键
     * @return 值
     */
    fun getDeep(key: String): Any?

    /**
     * Put all if exists
     *
     * @param map
     */
    fun putAllIfExists(map: Map<String, Any>)

    /**
     * 调用NativeFunction
     *
     * @param key 函数名
     * @param params 参数
     * @return 返回结果
     */
    fun invoke(key: String, vararg params: Any?): Any?

    /**
     * 导入其它脚本文件的上下文
     *
     * @param paths 脚本文件路径
     */
    fun import(vararg paths: String)

    /**
     * 选取一个对象作为当前对象
     *
     * 可通过 selector<R>() 获取
     *
     * @param obj 对象
     * @param R 类型
     * @return 对象
     */
    fun <R : Any> select(obj: R): R

    /**
     * 获取当前选择的对象 （强制类型转换）
     *
     * @param R 对象类型
     * @return 当前选择的对象
     */
    fun <R> selector(): R {
        return selectorSafely() ?: error("No Required Selector Selected!")
    }

    /**
     * 获取当前选择的对象 （安全类型转换）
     *
     * @param R 对象类型
     * @return 当前选择的对象
     */
    fun <R> AsahiContext.selectorSafely(): R? {
        return this["@selector"] as? R?
    }

    /**
     * 执行Quester
     *
     * @param R 返回类型
     * @return 返回值
     */
    fun <R> Quester<R>.run(): R {
        return execute()
    }


    /**
     * 执行Quester
     *
     * @param R 返回类型
     * @return 返回值
     */
    fun <R> Quester<R>.get(): R {
        return execute()
    }

    /**
     * Clone
     *
     * @return
     */
    fun clone(): AsahiContext

    /**
     * 获取当前上下文（其实就是自身）
     *
     * 这个函数存在的意义是让代码可读性变强
     *
     * @return
     */
    fun context(): AsahiContext {
        return this
    }

    /**
     * 当前程序结束时，要做的事
     *
     * @param exec 回调函数
     * @receiver
     */
    fun onExit(exec: () -> Unit)

    /**
     * 让一个任务随着程序结束而自动结束
     *
     * @param R
     * @return
     */
    fun <R> CompletableFuture<R>.autoCancelled(): CompletableFuture<R>

    /** 执行退出程序回调函数 */
    fun exit()

    /** 重置上下文 */
    fun reset()

    /**
     * 当前程序是否已结束
     *
     * @return 是否结束
     */
    fun isExit(): Boolean

    /** 开启执行时调试 */
    fun debugOn()

    /** 关闭执行时调试 */
    fun debugOff()

    /**
     * 如果在调试中
     *
     * @param todo 要干的事
     * @receiver
     */
    fun ifDebug(todo: () -> Unit)

    /**
     * 如果在调试中
     *
     * @param todo 要干的事
     * @param R
     * @return
     * @receiver
     */
    fun <R> R.ifDebug(todo: (R) -> Unit): R

    /**
     * 临时变量
     *
     * 键值对里的变量在 todo执行前塞入 执行后取出
     *
     * @param pairs 键值对
     * @param todo 执行内容
     * @param R
     * @return 执行结果
     * @receiver
     */
    fun <R> temp(vararg pairs: Pair<String, Any?>, todo: () -> R): R

    companion object {
        //给脚本用的 不用填参数 方便一些
        @JvmStatic
        fun create(): AsahiContext {
            return AsahiContextImpl.create()
        }

        /**
         * 创建上下文
         *
         * @param data MutableMap<String, Any> 基础Map，上下文会基于这个map创建
         * @return AsahiContext 创建好的上下文
         */
        @JvmStatic
        fun create(
            data: MutableMap<String, Any> = HashMap(),
        ): AsahiContext {
            return AsahiContextImpl.create(data)
        }

        internal val getters = LinkedList<Getter>()
        internal val setters = LinkedList<Setter>()

        @JvmStatic
        fun registerGetter(getter: Getter) {
            getters += getter
            getters.sorted()
        }

        @JvmStatic
        fun registerSetter(setter: Setter) {
            setters += setter
            getters.sorted()
        }
    }

    /**
     * 添加任务
     *
     * @param task 任务
     */
    fun addTask(task: CompletableFuture<*>)

    /** 等待所有任务完成 */
    fun awaitAllTask()
}