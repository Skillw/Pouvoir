package com.skillw.asahi.api.script.linking

/**
 * @className InvokerHolder
 *
 * @author Glom
 * @date 2023/1/22 19:06 Copyright 2023 user. All rights reserved.
 */
interface InvokerHolder {
    /** 可调用对象 当前上下文中的可调用对象 */
    val invokers: HashMap<String, Invoker>


    /**
     * Has invoker
     *
     * @param key
     * @return
     */
    fun hasInvoker(key: String): Boolean {
        return invokers.containsKey(key)
    }

    /**
     * Add invoker
     *
     * @param key
     * @param invoker
     */
    fun addInvoker(key: String, invoker: Invoker) {
        invokers[key] = invoker
    }
}