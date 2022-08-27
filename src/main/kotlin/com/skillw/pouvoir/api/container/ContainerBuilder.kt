package com.skillw.pouvoir.api.container

/**
 * Container builder
 *
 * @constructor Create empty Container builder
 * @property name
 */
open class ContainerBuilder(val name: String) {

    /**
     * Data
     *
     * @constructor Create empty Data
     * @property name
     * @property length
     * @property int
     * @property long
     * @property double
     * @property key
     */
    class Data(
        val name: String,
        val length: Int = 64,
        val int: Boolean = false,
        val long: Boolean = false,
        val double: Boolean = false,
        val key: Boolean = false,
    )

    /** Data list */
    val dataList = ArrayList<Data>()

    /** 添加数据列 */
    fun data(
        name: String,
        length: Int = 64,
        int: Boolean = false,
        long: Boolean = false,
        double: Boolean = false,
        key: Boolean = false,
    ) {
        dataList += Data(name, length, int, long, double, key)
    }

    /**
     * Flatten
     *
     * @param name
     * @constructor
     */
    class Flatten(name: String) : ContainerBuilder(name) {

        /**
         * Key
         *
         * @param name
         * @param length
         */
        fun key(name: String, length: Int = 64) {
            data(name, length, key = true)
        }

        /**
         * Value
         *
         * @param name
         * @param length
         * @param int
         * @param long
         * @param double
         */
        fun value(
            name: String,
            length: Int = 128,
            int: Boolean = false,
            long: Boolean = false,
            double: Boolean = false,
        ) {
            data(name, length, int, long, double)
        }

        /**
         * Fixed
         *
         * @return
         */
        fun fixed(): Flatten {
            when {
                dataList.size == 0 -> {
                    key("key")
                    value("value")
                }

                dataList.size != 2 -> {
                    error("Invalid container length")
                }
            }
            return this
        }
    }
}