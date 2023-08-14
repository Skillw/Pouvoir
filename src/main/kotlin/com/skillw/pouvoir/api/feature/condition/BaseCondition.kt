package com.skillw.pouvoir.api.feature.condition

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.plugin.map.component.Registrable
import org.bukkit.entity.LivingEntity
import java.util.*

/**
 * Base condition
 *
 * @constructor
 * @property key 键
 * @property type 类型
 */
abstract class BaseCondition(override val key: String) :
    Registrable<String>, Condition, ConditionReader {

    /** 是否在下次重载时自动注销 */
    var release = false


    abstract override fun parameters(text: String): Map<String, Any>?

    /**
     * Builder
     *
     * @constructor Create empty Builder
     * @property key 键
     * @property type 类型
     */
    class Builder(val key: String) {
        /** 是否在下次重载时自动注销 */
        var release = false


        private val conditions = ArrayList<Condition>()

        private val conditionReaders = ArrayList<ConditionReader>()

        /**
         * Condition
         *
         * @param condition
         */
        fun condition(
            condition: Condition,
        ) {
            conditions.add(condition)
        }

        fun parameters(
            reader: ConditionReader,
        ) {
            conditionReaders.add(reader)
        }

        /**
         * Build
         *
         * @return
         */
        fun build(): BaseCondition {
            return object : BaseCondition(key) {
                override fun parameters(text: String): Map<String, Any> {
                    val map = HashMap<String, Any>()
                    conditionReaders.forEach { it.parameters(text)?.let { it1 -> map.putAll(it1) } }
                    return map
                }

                override fun condition(entity: LivingEntity?, parameters: Map<String, Any>): Boolean {
                    return conditions.all {
                        it.condition(entity, parameters)
                    }
                }

                init {
                    this.release = this@Builder.release
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun createCondition(
            key: String,
            init: Builder.() -> Unit,
        ): BaseCondition {
            val builder = Builder(key)
            builder.init()
            return builder.build()
        }
    }

    override fun register() {
        Pouvoir.conditionManager.register(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BaseCondition) return false

        if (key != other.key) return false
        return release == other.release
    }

    override fun hashCode(): Int {
        return key.hashCode()
    }

    override fun toString(): String {
        return "Condition { key: $key }"
    }


}

