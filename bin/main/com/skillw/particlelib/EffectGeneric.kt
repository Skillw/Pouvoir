package com.skillw.particlelib

import org.bukkit.entity.Entity
import taboolib.common.util.Location
import taboolib.common.util.Vector
import com.skillw.particlelib.pobject.*

/**
 * TODO
 * - EffectGroup
 */

/**
 * 创建一个弧
 *
 * @param origin 弧的中心
 * @param startAngle 弧的起始角度
 * @param angle 弧的角度
 * @param radius 弧的半径
 * @param step 弧的步长
 * @param period 特效周期(如果需要可以使用)
 */
fun createArc(
    origin: Location,
    startAngle: Double = 0.0,
    angle: Double = 30.0,
    radius: Double = 1.0,
    step: Double = 1.0,
    period: Long = 20,
): Arc {
    return Arc(origin, startAngle, angle, radius, step, period)
}

/**
 * 创建一个星型线
 *
 * @param origin 中心点
 * @param radius 半径
 * @param step 步长
 * @param period 特效周期(如果需要可以使用)
 */
fun createAstroid(
    origin: Location,
    radius: Double = 1.0,
    step: Double = 1.0,
    period: Long = 20,
): Astroid {
    return Astroid(radius, origin).also {
        it.step = step
        it.period = period
    }
}

/**
 * 创建一个圆
 *
 * @param origin 圆心
 * @param radius 半径
 * @param step 每个粒子的间隔(也即步长)
 * @param period 特效周期(如果需要可以使用)
 */
fun createCircle(
    origin: Location,
    radius: Double = 1.0,
    step: Double = 1.0,
    period: Long = 20,
): Circle {
    return Circle(origin, radius, step, period)
}

/**
 * 创建一个立方体
 *
 * @param min 最小点
 * @param max 最大点
 * @param step 每个粒子的间隔(也即步长)
 * @param period 特效周期(如果需要可以使用)
 */
fun createCube(
    min: Location,
    max: Location,
    step: Double = 1.0,
    period: Long = 20,
): Cube {
    return Cube(min, max, step).also { it.period = period }
}

/**
 * 创建实心圆
 *
 * @param origin 圆心
 * @param radius 半径
 * @param sample 粒子数量
 * @param period 特效周期(如果需要可以使用)
 */
fun createFilledCircle(
    origin: Location,
    radius: Double = 1.0,
    sample: Int = 100,
    period: Long = 20,
): FilledCircle {
    return FilledCircle(origin, radius, sample).also { it.period = period }
}

/**
 * 创建网格
 *
 * @param minLoc 起点
 * @param maxLoc 终点
 * @param length 网格长度
 * @param period 特效周期(如果需要可以使用)
 */
fun createGrid(
    minLoc: Location,
    maxLoc: Location,
    length: Double = 1.2,
    period: Long = 20,
): Grid {
    return Grid(minLoc, maxLoc, length).also { it.period = period }
}

/**
 * 表示一颗心
 *
 * @param origin 原点
 * @param xScaleRate X轴缩放比率
 * @param yScaleRate Y轴缩放比率
 * @author Zoyn IceCold 构造一个心形线
 */
fun createHeart(
    origin: Location, xScaleRate: Double = 1.0, yScaleRate: Double = 1.0, period: Long = 20,
): Heart {
    return Heart(origin, xScaleRate, yScaleRate).also { it.period = period }
}

/**
 * 创建一条线
 *
 * @param start 起始点
 * @param end 结束点
 * @param step 每个粒子的间隔(也即步长)
 * @param period 特效周期(如果需要可以使用)
 */
fun createLine(
    start: Location,
    end: Location,
    step: Double = 1.0,
    period: Long = 20,
): Line {
    return Line(start, end, step, period)
}

/**
 * 一朵莲花
 *
 * @param origin Location
 * @param period Long
 * @return Lotus
 */
fun createLotus(
    origin: Location,
    period: Long = 20,
): Lotus {
    return Lotus(origin).also { it.period = period }
}

/**
 * 创建一个正多边形
 *
 * @param radius 半径
 * @param sides 边数
 * @param step 每个粒子的间隔(也即步长)
 * @param period 特效周期(如果需要可以使用)
 */
fun createPolygon(
    origin: Location,
    radius: Double = 1.0,
    sides: Int = 3,
    step: Double = 1.0,
    period: Long = 20,
): Polygon {
    return Polygon(sides, origin, step, radius).also {
        it.period = period
    }
}

/**
 * 一个射线
 *
 * 调用show方法开始射出
 *
 * @param direction 方向
 * @param maxLength 长度
 * @param step 步长
 * @param range 每个点判断怪物在其上的半径
 * @param stopType 结束类型
 * @param onHit 命中实体处理
 * @param entityFilter 命中实体过滤器
 * @param onEnd 射线结束处理
 * @author Zoyn IceCold Glom_
 */
fun createRay(
    origin: Location,
    direction: Vector,
    maxLength: Double,
    step: Double = 0.2,
    range: Double = 0.5,
    stopType: Ray.RayStopType = Ray.RayStopType.MAX_LENGTH,
    hitEntities: Boolean = false,
    onHit: (Entity) -> Unit = { },
    entityFilter: (Entity) -> Boolean = { true },
    onEnd: (Entity) -> Unit = { },
): Ray {
    return Ray(origin, direction, maxLength, step, range, stopType, hitEntities, onHit, entityFilter, onEnd)
}

/**
 * 表示一个星星
 *
 * @param origin 起点
 * @param radius 星星半径
 * @param step 步长
 * @author Zoyn
 */
fun createStar(
    origin: Location,
    radius: Double = 1.0,
    step: Double = 0.05,
    period: Long = 20,
): Star {
    return Star(origin, radius, step).also { it.period = period }
}

/**
 * 创建一个球
 *
 * @param origin 球心
 * @param radius 半径
 * @param sample 粒子数量
 * @param period 特效周期(如果需要可以使用)
 */
fun createSphere(
    origin: Location,
    radius: Double = 1.0,
    sample: Int = 100,
    period: Long = 20,
): Sphere {
    return Sphere(origin, sample, radius).also { it.period = period }
}


/**
 * 构造一个翅膀
 *
 * @param origin 起点
 * @param pattern 图案
 * @param minRotAngle 起始扇动角度（最小
 * @param maxRotAngle 最大扇动角度（最大
 * @param interval 粒子之间的距离
 */
fun createWing(
    origin: Location,
    pattern: List<String>,
    minRotAngle: Double = 30.0,
    maxRotAngle: Double = 60.0,
    interval: Double = 0.2,
    period: Long = 20,
): Wing {
    return Wing(origin, pattern, minRotAngle, maxRotAngle, interval).also { it.period = period }
}