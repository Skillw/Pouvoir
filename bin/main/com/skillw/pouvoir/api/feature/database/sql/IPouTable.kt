package com.skillw.pouvoir.api.feature.database.sql

import taboolib.module.database.*
import javax.sql.DataSource

/**
 * IPouTable Pou表
 *
 * 为了使用TLib6的database模块而生的一个接口
 *
 * 对应着sql数据库中的一张表 可以对表进行所有相关操作
 *
 * @param T Host类型
 * @param E ColumnBuilder类型
 * @constructor Create empty I pou table
 */
interface IPouTable<T : Host<E>, E : ColumnBuilder> {
    /** 表名 */
    val name: String

    /** 列名 */
    val columns: ArrayList<Column>

    /** Primary key for legacy */
    val primaryKeyForLegacy: ArrayList<String>

    /** 数据源 */
    val dataSource: DataSource

    /**
     * 添加列
     *
     * @param name 列名
     * @param func 处理
     * @return
     * @receiver
     */
    fun column(name: String? = null, func: E.() -> Unit): Table<T, E>

    /**
     * 选取数据
     *
     * @param func 选取操作
     * @return 结果
     * @receiver
     */
    fun select(func: ActionSelect.() -> Unit): QueryTask

    /**
     * 寻找数据
     *
     * @param func 寻找操作
     * @return 结果
     * @receiver
     */
    fun find(func: ActionSelect.() -> Unit): Boolean

    /**
     * 更新数据
     *
     * @param func 更新操作
     * @return
     * @receiver
     */
    fun update(func: ActionUpdate.() -> Unit): Int

    /**
     * 删除数据
     *
     * @param func 删除操作
     * @return
     * @receiver
     */
    fun delete(func: ActionDelete.() -> Unit): Int

    /**
     * 插入操作
     *
     * @param keys 列名表（若为空则一一对应表的列名）
     * @param func 插入操作
     * @return
     * @receiver
     */
    fun insert(vararg keys: String, func: ActionInsert.() -> Unit): Int

    /**
     * 工作空间
     *
     * @param func 处理
     * @return 结果
     * @receiver
     */
    fun workspace(func: Query.() -> Unit): QueryTask

    /** 关闭数据源 */
    fun close()

    /** 创建当前PouTable对应的表 */
    fun createTable()
}