package com.skillw.pouvoir.internal.feature.compat.placeholder

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.Pouvoir.scriptManager
import com.skillw.pouvoir.api.feature.placeholder.PouPlaceHolder
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import com.skillw.pouvoir.util.plugin.Pair
import com.skillw.pouvoir.util.protectedSplit
import com.skillw.pouvoir.util.toArgs
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import taboolib.platform.compat.replacePlaceholder

@AutoRegister
object PouvoirHooker : PouPlaceHolder("pou", Pouvoir) {
    override fun onPlaceHolderRequest(params: String, entity: LivingEntity, def: String): String {
        if (params.isBlank()) return def
        var argsStr = params.replace("@", "%")
        if (entity is Player) {
            argsStr = argsStr.replacePlaceholder(entity)
        }
        val args = argsStr.protectedSplit('_', Pair('[', ']'))
        if (args.isEmpty()) return def
        when (args[0].lowercase()) {
            "run" -> {
                if (args.isEmpty()) return def
                val scriptPath = args[1]
                val spilt =
                    argsStr.replace("run_$scriptPath\\_", "").replace("-", "_").toArgs()
                        .filter { it.isNotEmpty() && it.isNotBlank() }
                val finalArgs = Array(spilt.size) {
                    Pouvoir.placeholderManager.replace(entity, spilt[it])
                }
                return scriptManager.invoke<String>(
                    scriptPath,
                    arguments = hashMapOf("entity" to entity, "sender" to entity, "args" to finalArgs),
                    null,
                    parameters = finalArgs
                ).toString()
            }
        }
        return def
    }


}
