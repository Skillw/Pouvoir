package com.skillw.pouvoir.internal.hook

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.Pouvoir.scriptManager
import com.skillw.pouvoir.api.placeholder.PouPlaceHolder
import com.skillw.pouvoir.util.StringUtils.protectedSplit
import com.skillw.pouvoir.util.StringUtils.toArgs
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import taboolib.platform.compat.replacePlaceholder

object PouvoirHooker : PouPlaceHolder("pou", Pouvoir) {
    override fun onPlaceHolderRequest(params: String, livingEntity: LivingEntity, def: String): String {
        if (params.isBlank()) return def
        var argsStr = params.replace("@", "%")
        if (livingEntity is Player) {
            argsStr = argsStr.replacePlaceholder(livingEntity)
        }
        val args = argsStr.protectedSplit('_', Pair('[', ']'))
        if (args.isEmpty()) return def
        when (args[0].lowercase()) {
            "run" -> {
                args.removeAt(0)
                when {
                    args.size < 1 -> return def
                }
                val scriptPath = args[0]
                val spilt: Array<String> = if (args.size > 1) params.replace("run_", "").toArgs() else emptyArray()
                val finalArgs = Array(spilt.size) {
                    Pouvoir.pouPlaceHolderAPI.replace(livingEntity, spilt[it])
                }
                return scriptManager.invokePathWithFunction(
                    scriptPath,
                    argsMap = hashMapOf("entity" to livingEntity, "args" to finalArgs)
                )
                    .toString()
            }
        }
        return def
    }


}
