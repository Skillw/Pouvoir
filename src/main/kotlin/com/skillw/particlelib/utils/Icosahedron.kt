package com.skillw.particlelib.utils

import taboolib.common.util.Vector
import kotlin.math.sqrt

class Icosahedron {
    private val triangles: MutableList<Triangle> = ArrayList()
    fun drawIcosahedron(depth: Int, radius: Float) {
        for (i in tindx.indices) {
            subdivide(
                vdata[tindx[i][0]],  //
                vdata[tindx[i][1]],  //
                vdata[tindx[i][2]], depth, radius
            )
        }
    }

    private fun drawTriangle(vA0: DoubleArray, vB1: DoubleArray, vC2: DoubleArray, radius: Float) {
        val triangle = Triangle(
            Vector(vA0[0], vA0[1], vA0[2]).multiply(radius),
            Vector(vB1[0], vB1[1], vB1[2]).multiply(radius),
            Vector(vC2[0], vC2[1], vC2[2]).multiply(radius)
        )
        triangles.add(triangle)
    }

    private fun subdivide(vA0: DoubleArray, vB1: DoubleArray, vC2: DoubleArray, depth: Int, radius: Float) {
        val vAB = DoubleArray(3)
        val vBC = DoubleArray(3)
        val vCA = DoubleArray(3)
        var i: Int
        if (depth == 0) {
            drawTriangle(vA0, vB1, vC2, radius)
            return
        }
        i = 0
        while (i < 3) {
            vAB[i] = (vA0[i] + vB1[i]) / 2
            vBC[i] = (vB1[i] + vC2[i]) / 2
            vCA[i] = (vC2[i] + vA0[i]) / 2
            i++
        }
        val modAB = mod(vAB)
        val modBC = mod(vBC)
        val modCA = mod(vCA)
        i = 0
        while (i < 3) {
            vAB[i] /= modAB
            vBC[i] /= modBC
            vCA[i] /= modCA
            i++
        }
        subdivide(vA0, vAB, vCA, depth - 1, radius)
        subdivide(vB1, vBC, vAB, depth - 1, radius)
        subdivide(vC2, vCA, vBC, depth - 1, radius)
        subdivide(vAB, vBC, vCA, depth - 1, radius)
    }

    fun getTriangles(): List<Triangle> {
        return triangles
    }

    class Triangle(var point1: Vector, var point2: Vector, var point3: Vector)
    companion object {
        var X = 0.525731112119133606
        var Z = 0.850650808352039932
        var vdata = arrayOf(
            doubleArrayOf(-X, +0.0, +Z),
            doubleArrayOf(+X, +0.0, +Z),
            doubleArrayOf(-X, +0.0, -Z),
            doubleArrayOf(+X, +0.0, -Z),
            doubleArrayOf(+0.0, +Z, +X),
            doubleArrayOf(+0.0, +Z, -X),
            doubleArrayOf(+0.0, -Z, +X),
            doubleArrayOf(+0.0, -Z, -X),
            doubleArrayOf(+Z, +X, +0.0),
            doubleArrayOf(-Z, +X, +0.0),
            doubleArrayOf(+Z, -X, +0.0),
            doubleArrayOf(-Z, -X, +0.0)
        )
        var tindx = arrayOf(
            intArrayOf(0, 4, 1),
            intArrayOf(0, 9, 4),
            intArrayOf(9, 5, 4),
            intArrayOf(4, 5, 8),
            intArrayOf(4, 8, 1),
            intArrayOf(8, 10, 1),
            intArrayOf(8, 3, 10),
            intArrayOf(5, 3, 8),
            intArrayOf(5, 2, 3),
            intArrayOf(2, 7, 3),
            intArrayOf(7, 10, 3),
            intArrayOf(7, 6, 10),
            intArrayOf(7, 11, 6),
            intArrayOf(11, 0, 6),
            intArrayOf(0, 1, 6),
            intArrayOf(6, 1, 10),
            intArrayOf(9, 0, 11),
            intArrayOf(9, 11, 2),
            intArrayOf(9, 2, 5),
            intArrayOf(7, 2, 11)
        )

        fun mod(v: DoubleArray): Double {
            return sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2])
        }
    }
}