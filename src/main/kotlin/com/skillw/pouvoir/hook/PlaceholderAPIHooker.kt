package com.skillw.pouvoir.hook

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.util.StringUtils.protectedSplit
import org.bukkit.entity.Player
import taboolib.platform.compat.PlaceholderExpansion
import taboolib.platform.compat.replacePlaceholder

object PlaceholderAPIHooker : PlaceholderExpansion {
    override val identifier = "pou"
    override fun onPlaceholderRequest(player: Player?, args: String): String {
        return if (player == null)
            args
        else
            handle(player, args)
    }


    private fun handle(player: Player, params: String): String {
        if (params.isBlank()) return ""
        val args = params.replace("@", "%").replacePlaceholder(player).protectedSplit('_', Pair('[', ']'))
        if (args.isEmpty()) return ""
        when (args[0].lowercase()) {
            "js" -> {
                when {
                    args.size < 2 -> return "Can not found the path"
                    args.size < 3 -> return "Can not found the function name"
                }
                val path = args[1]
                val function = args[2]
                val spilt: Array<String> = if (args.size > 3) {
                    val origin = args[3]
                    if (origin.contains(",")) {
                        origin.split(",").toTypedArray()
                    } else arrayOf(origin)
                } else emptyArray()
                val finalArgs = Array(spilt.size) {
                    spilt[it].replacePlaceholder(player)
                }
                val optional = Pouvoir.scriptManager.search(args[1])
                if (!optional.isPresent) {
                    return "Can not found the script file: ${args[1]}"
                }
                val script = optional.get()
                return script.invoke(function, argsMap = hashMapOf(Pair("args", finalArgs))).toString()
            }
        }
        return "N/A"
    }
}
