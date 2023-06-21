package com.skillw.particlelib.utils.matrix

import taboolib.common.util.Location
import taboolib.common.util.Vector
import java.util.*

/**
 * 表示一个 m*n 的矩阵
 *
 * 在该类中, 所有的乘法操作都是左乘
 *
 * @author Zoyn
 */
class Matrix {
    private val m: Array<DoubleArray>

    constructor(row: Int, column: Int) {
        m = Array(row) { DoubleArray(column) }
    }

    constructor(m: Array<DoubleArray>) {
        this.m = m
    }

    constructor(matrix: Matrix) {
        m = matrix.getAsArray()
    }

    fun getRow(): Int {
        return m.size
    }

    fun getColumn(): Int {
        return m[0].size
    }

    fun getAsArray(): Array<DoubleArray> {
        return m
    }

    operator fun get(row: Int, column: Int): Double {
        return m[row - 1][column - 1]
    }

    /**
     * 通过给定的值设定矩阵内对应的数值
     *
     * @param row 行数
     * @param column 列数
     * @param value 数值
     * @return [Matrix]
     */
    operator fun set(row: Int, column: Int, value: Double): Matrix {
        m[row - 1][column - 1] = value
        return this
    }

    /**
     * 填充矩阵的某一行为同一实数
     *
     * @param row 行数
     * @param value 实数
     * @return [Matrix]
     */
    fun fill(row: Int, value: Double): Matrix {
        Arrays.fill(m[row - 1], value)
        return this
    }

    /**
     * 取出矩阵中单独的一行
     *
     * @param row 行数
     * @return 对应行所成的数组
     */
    fun getRowAsArray(row: Int): DoubleArray {
        return Arrays.copyOf(m[row - 1], getColumn())
    }

    /**
     * 取矩阵中单独的一列
     *
     * @param column 列数
     * @return 列所成的数组
     */
    fun getColumnAsArray(column: Int): DoubleArray {
        val m = DoubleArray(getRow())
        for (row in 0 until getRow()) {
            m[row] = get(row + 1, column)
        }
        return m
    }

    fun isSameRow(matrix: Matrix): Boolean {
        return getRow() == matrix.getRow()
    }

    fun isSameColumn(matrix: Matrix): Boolean {
        return getColumn() == matrix.getColumn()
    }

    fun isSameRowAndColumn(matrix: Matrix): Boolean {
        return isSameRow(matrix) && isSameColumn(matrix)
    }

    /**
     * 将该矩阵进行转置变换
     *
     * @return [Matrix]
     */
    fun invert(): Matrix {
        val n = Array(getColumn()) { DoubleArray(getRow()) }
        for (i in 0 until getRow()) {
            for (j in 0 until getColumn()) {
                n[j][i] = m[i][j]
            }
        }
        return Matrix(n)
    }

    /**
     * 将两个矩阵相加
     *
     * 注意: 本矩阵的大小要等于另一矩阵的大小
     *
     * @param matrix 给定的矩阵
     * @return [Matrix]
     */
    operator fun plus(matrix: Matrix): Matrix {
        require(isSameRowAndColumn(matrix)) { "两矩阵大小不相同!" }
        val n = matrix.getAsArray()
        val result = Array(getRow()) { DoubleArray(getColumn()) }
        for (row in 0 until getRow()) {
            for (column in 0 until getColumn()) {
                result[row][column] = m[row][column] + n[row][column]
            }
        }
        //        this.m = result;
        return Matrix(result)
    }

    /**
     * 将该矩阵乘以一个实数
     *
     * @param value 给定的数
     * @return [Matrix]
     */
    fun multiply(value: Double): Matrix {
        val result = Array(getRow()) { DoubleArray(getColumn()) }
        for (row in 0 until getRow()) {
            for (column in 0 until getColumn()) {
                result[row][column] = m[row][column] * value
            }
        }
        return Matrix(result)
    }

    /**
     * 将该矩阵乘以另一个矩阵
     *
     * 注意: 本矩阵的列数要等于另外一个矩阵的行数
     *
     * @param matrix 给定的另一矩阵
     * @return [Matrix]
     */
    fun multiply(matrix: Matrix?): Matrix {
        require(getColumn() == matrix!!.getRow()) { "原矩阵的列数不等于新矩阵的行数" }
        val n = matrix.getAsArray()
        val result = Array(getRow()) { DoubleArray(matrix.getColumn()) }
        for (row in 0 until getRow()) {
            for (column in 0 until matrix.getColumn()) {
                val x = getRowAsArray(row + 1)
                val y = matrix.getColumnAsArray(column + 1)
                for (i in x.indices) {
                    result[row][column] += x[i] * y[i]
                }
            }
        }
        return Matrix(result)
    }

    /** 将矩阵漂亮的打印出来 */
    fun prettyPrinting() {
        for (doubles in m) {
            println(doubles.contentToString())
        }
    }

    /**
     * 将本矩阵的变换作用至给定的坐标上
     *
     * @param location 给定的坐标
     * @return [Location]
     */
    fun applyLocation(location: Location): Location {
        if (getRow() == 2 && getColumn() == 2) {
            return applyIn2DLocation(location)
        } else if (getRow() == 3 && getColumn() == 3) {
            return applyIn3DLocation(location)
        }
        throw IllegalArgumentException("当前矩阵非 2*2 或 3*3 的方阵")
    }

    /**
     * 将本矩阵的变换作用至给定的向量上
     *
     * @param vector 给定的向量
     * @return [Location]
     */
    fun applyVector(vector: Vector): Vector {
        if (getRow() == 2 && getColumn() == 2) {
            return applyIn2DVector(vector)
        } else if (getRow() == 3 && getColumn() == 3) {
            return applyIn3DVector(vector)
        }
        throw IllegalArgumentException("当前矩阵非 2*2 或 3*3 的方阵")
    }

    private fun applyIn2DVector(vector: Vector): Vector {
        val x = vector.x
        val z = vector.z
        val ax = getAsArray()[0][0] * x
        val ay = getAsArray()[0][1] * z
        val bx = getAsArray()[1][0] * x
        val by = getAsArray()[1][1] * z
        return Vector(ax + ay, vector.y, bx + by)
    }

    private fun applyIn3DVector(vector: Vector): Vector {
        val x = vector.x
        val y = vector.y
        val z = vector.z
        val ax = getAsArray()[0][0] * x
        val ay = getAsArray()[0][1] * y
        val az = getAsArray()[0][2] * z
        val bx = getAsArray()[1][0] * x
        val by = getAsArray()[1][1] * y
        val bz = getAsArray()[1][2] * z
        val cx = getAsArray()[2][0] * x
        val cy = getAsArray()[2][1] * y
        val cz = getAsArray()[2][2] * z
        return Vector(ax + ay + az, bx + by + bz, cx + cy + cz)
    }

    private fun applyIn2DLocation(location: Location): Location {
        val x = location.x
        val z = location.z
        val ax = getAsArray()[0][0] * x
        val ay = getAsArray()[0][1] * z
        val bx = getAsArray()[1][0] * x
        val by = getAsArray()[1][1] * z
        return Location(location.world, ax + ay, location.y, bx + by, location.yaw, location.pitch)
    }

    private fun applyIn3DLocation(location: Location): Location {
        val x = location.x
        val y = location.y
        val z = location.z
        val ax = getAsArray()[0][0] * x
        val ay = getAsArray()[0][1] * y
        val az = getAsArray()[0][2] * z
        val bx = getAsArray()[1][0] * x
        val by = getAsArray()[1][1] * y
        val bz = getAsArray()[1][2] * z
        val cx = getAsArray()[2][0] * x
        val cy = getAsArray()[2][1] * y
        val cz = getAsArray()[2][2] * z
        return Location(location.world, ax + ay + az, bx + by + bz, cx + cy + cz, location.yaw, location.pitch)
    }
}