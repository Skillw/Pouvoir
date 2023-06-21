package com.skillw.particlelib.utils.matrix

import kotlin.math.cos
import kotlin.math.sin

/**
 * 与 [Matrix] 相关的静态实用方法
 *
 * @author Zoyn
 */
object Matrixs {
    /**
     * 通过给定的行列数返回一个全零矩阵
     *
     * @param row 行数
     * @param column 列数
     * @return [Matrix]
     */
    fun zeros(row: Int, column: Int): Matrix {
        return Matrix(row, column)
    }

    /**
     * 通过给定的行列数返回一个全一矩阵
     *
     * @param row 行数
     * @param column 列数
     * @return [Matrix]
     */
    fun ones(row: Int, column: Int): Matrix {
        val matrix = Matrix(row, column)
        for (i in 0 until row) {
            matrix.fill(i + 1, 1.0)
        }
        return matrix
    }

    /**
     * 通过给定的行列数返回一个单位矩阵
     *
     * @param row 行数
     * @param column 列数
     * @return [Matrix]
     */
    fun eyes(row: Int, column: Int): Matrix {
        val result = Array(row) { DoubleArray(column) }
        for (i in 0 until row) {
            for (j in 0 until column) {
                // 行列相等时则为 1
                if (i == j) {
                    result[i][j] = 1.0
                    continue
                }
                result[i][j] = 0.0
            }
        }
        return Matrix(result)
    }

    /**
     * 通过给定的角度返回一个平面旋转矩阵
     *
     * @param theta 旋转角度
     * @return [Matrix]
     */
    fun rotate2D(theta: Double): Matrix {
        val m = Array(2) { DoubleArray(2) }
        val radians = Math.toRadians(-theta)
        val cos = cos(radians)
        val sin = sin(radians)
        m[0][0] = cos
        m[0][1] = -sin
        m[1][0] = sin
        m[1][1] = cos
        return Matrix(m)
    }

    /**
     * 通过给定的角度返回一个关于X轴的旋转矩阵
     *
     * 注意：该方法会返回3阶方阵
     *
     * @param theta 旋转角度
     * @return [Matrix]
     */
    fun rotateAroundXAxis(theta: Double): Matrix {
        val matrix = eyes(3, 3)
        val radians = Math.toRadians(-theta)
        val cos = cos(radians)
        val sin = sin(radians)
        matrix.set(2, 2, cos)
            .set(2, 3, sin)
            .set(3, 2, -sin)[3, 3] = cos
        return matrix
    }

    /**
     * 通过给定的角度返回一个关于Z轴的旋转矩阵
     *
     * 注意：该方法会返回3阶方阵
     *
     * @param theta 旋转角度
     * @return [Matrix]
     */
    fun rotateAroundYAxis(theta: Double): Matrix {
        val matrix = eyes(3, 3)
        val radians = Math.toRadians(-theta)
        val cos = cos(radians)
        val sin = sin(radians)
        matrix.set(1, 1, cos)
            .set(1, 3, -sin)
            .set(3, 1, sin)[3, 3] = cos
        return matrix
    }

    /**
     * 通过给定的角度返回一个关于Z轴的旋转矩阵
     *
     * 注意：该方法会返回3阶方阵
     *
     * @param theta 旋转角度
     * @return [Matrix]
     */
    fun rotateAroundZAxis(theta: Double): Matrix {
        val matrix = eyes(3, 3)
        val radians = Math.toRadians(-theta)
        val cos = cos(radians)
        val sin = sin(radians)
        matrix.set(1, 1, cos)
            .set(1, 2, sin)
            .set(2, 1, -sin)[2, 2] = cos
        return matrix
    }

    /**
     * 建立一个放大或缩小的矩阵
     *
     * @param row 行数
     * @param column 列数
     * @param value 放大或缩小的值
     * @return [Matrix]
     */
    fun scale(row: Int, column: Int, value: Double): Matrix {
        return eyes(row, column).multiply(value)
    }
}