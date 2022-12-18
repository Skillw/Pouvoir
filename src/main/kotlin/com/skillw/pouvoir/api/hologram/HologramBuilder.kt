package com.skillw.pouvoir.api.hologram

import com.skillw.pouvoir.internal.feature.hologram.Hologram
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import taboolib.common.platform.function.submit
import taboolib.platform.compat.replacePlaceholder
import java.util.*


/**
 * Hologram builder
 *
 * @constructor Create empty Hologram builder
 * @property location
 */
class HologramBuilder(private val location: Location) {
    private var content: MutableList<String> = LinkedList()
    private val viewers: MutableSet<Player> = Collections.synchronizedSet(HashSet())
    private var stay: Long = -1
    private var time: Int = -1
    private var each: Vector? = null

    /**
     * Stay
     *
     * 持续时间
     *
     * @param stay
     * @return
     */
    fun stay(stay: Long): HologramBuilder {
        this.stay = stay
        return this
    }

    /**
     * Content
     *
     * 内容
     *
     * @param content 内容
     * @return 自身
     */
    fun content(content: Collection<String>): HologramBuilder {
        this.content.clear()
        this.content.addAll(content)
        return this
    }

    /**
     * Viewers
     *
     * @param viewers 可以看到的玩家
     * @return 自身
     */
    fun viewers(vararg viewers: Player): HologramBuilder {
        this.viewers.clear()
        this.viewers.addAll(viewers)
        return this
    }

    /**
     * Viewers
     *
     * @param viewers 可以看到的玩家
     * @return 自身
     */
    fun viewers(viewers: MutableList<Player>): HologramBuilder {
        this.viewers.clear()
        this.viewers.addAll(viewers)
        return this
    }

    /**
     * 添加观察者
     *
     * @param player
     */
    fun addViewer(player: Player) {
        this.viewers.add(player)
    }

    /**
     * 动画
     *
     * @param time 持续时间
     * @param finalLocation 最终地点
     * @return 自身
     */
    fun animation(time: Int, finalLocation: Location): HologramBuilder {
        if (stay == -1L || time == -1) return this
        val reduce = finalLocation.subtract(location).toVector()
        val multiply = 1.0.div(time)
        this.each = reduce.clone().multiply(multiply)
        this.time = time
        return this
    }

    /**
     * 换位符
     *
     * @param player
     * @return 自身
     */
    fun placeholder(player: Player): HologramBuilder {
        this.content.replacePlaceholder(player)
        return this
    }

    /**
     * 构建全息
     *
     * @return 构建好的Hologram
     */
    fun build(): Hologram {
        val hologram = Hologram(location, content, viewers)
        if (stay != -1L) {
            each?.also { vector ->
                var count = 0
                submit(async = true, period = stay / time) {
                    if (count > time - 1) {
                        hologram.delete()
                        cancel()
                    }
                    hologram.teleport(location.clone().add(vector.clone().multiply(count)))
                    count++
                }
            } ?: submit(delay = stay) {
                hologram.delete()
                cancel()
            }
        }
        return hologram
    }

}