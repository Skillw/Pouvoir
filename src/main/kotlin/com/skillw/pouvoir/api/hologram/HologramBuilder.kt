package com.skillw.pouvoir.api.hologram

import com.skillw.pouvoir.internal.hologram.Hologram
import io.netty.util.internal.ConcurrentSet
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import taboolib.common.platform.function.submit
import taboolib.platform.compat.replacePlaceholder
import java.util.*

class HologramBuilder(private val location: Location) {
    private var content: MutableList<String> = LinkedList()
    private val viewers: MutableSet<Player> = ConcurrentSet()
    private var stay: Long = -1
    private var time: Int = -1
    private var each: Vector? = null


    fun stay(stay: Long): HologramBuilder {
        this.stay = stay
        return this
    }

    fun content(content: MutableList<String>): HologramBuilder {
        this.content.clear()
        this.content.addAll(content)
        return this
    }

    fun viewers(vararg viewers: Player): HologramBuilder {
        this.viewers.clear()
        this.viewers.addAll(viewers)
        return this
    }

    fun viewers(viewers: MutableList<Player>): HologramBuilder {
        this.viewers.clear()
        this.viewers.addAll(viewers)
        return this
    }

    fun addViewer(player: Player) {
        this.viewers.add(player)
    }

    fun animation(time: Int, finalLocation: Location): HologramBuilder {
        if (stay == -1L || time == -1) return this
        val reduce = finalLocation.subtract(location).toVector()
        val multiply = 1.0.div(time)
        this.each = reduce.clone().multiply(multiply)
        this.time = time
        return this
    }

    fun placeholder(player: Player): HologramBuilder {
        this.content.replacePlaceholder(player)
        return this
    }

    fun build(): Hologram {
        val hologram = Hologram(location, content, viewers)
        if (stay != -1L) {
            if (each == null)
                submit(async = true, delay = stay) {
                    hologram.delete()
                }
            else {
                var count = 0
                submit(async = true, period = stay / time) {
                    if (count > time - 1) {
                        hologram.delete()
                        cancel()
                    }
                    hologram.teleport(location.clone().add(each!!.clone().multiply(count)))
                    count++
                }
            }
        }
        return hologram
    }

}