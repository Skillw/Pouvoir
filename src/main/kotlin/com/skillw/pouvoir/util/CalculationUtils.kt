package com.skillw.pouvoir.util

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.annotation.ScriptTopLevel
import com.skillw.pouvoir.internal.manager.PouConfig
import com.skillw.pouvoir.internal.script.javascript.PouJavaScriptEngine
import com.skillw.pouvoir.util.StringUtils.replacement
import org.bukkit.entity.LivingEntity
import taboolib.common.platform.function.warning
import taboolib.common5.Coerce
import java.math.BigDecimal

object CalculationUtils {

    @ScriptTopLevel
    @JvmStatic
    fun String.calculate(entity: LivingEntity? = null, replacements: Map<String, String>? = null): BigDecimal {
        return calculate(Pouvoir.pouPlaceHolderAPI.replace(entity, replacement(replacements ?: HashMap())))
    }


    @JvmName("calculateToDouble")
    @JvmStatic
    fun String.calculateDouble(entity: LivingEntity? = null, replacements: Map<String, String>? = null): Double {
        return calculate(entity, replacements).setScale(PouConfig.scale, BigDecimal.ROUND_HALF_UP).toDouble()
    }

    private val calEngine by lazy {
        PouJavaScriptEngine.engine
    }

    @JvmStatic
    fun calculate(input: String): BigDecimal {
        return try {
            val optional = Coerce.asDouble(calEngine.eval(input))
            if (!optional.isPresent) error("Wrong calculation formula! $input");
            BigDecimal.valueOf(optional.get())
        } catch (e: Exception) {
            warning("Wrong calculation formula! $input")
            BigDecimal.valueOf(0.0)
        }
    }
}