package com.skillw.pouvoir.internal.manager

import com.skillw.pouvoir.Pouvoir
import com.skillw.pouvoir.api.manager.sub.PlayerDataManager
import taboolib.common.io.newFile
import taboolib.common.platform.function.getDataFolder
import taboolib.common.platform.function.pluginId
import taboolib.expansion.Database
import taboolib.expansion.TypeSQL
import taboolib.expansion.TypeSQLite
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.database.HostSQL
import java.io.File

object PlayerDataManagerImpl : PlayerDataManager() {
    override val key = "PlayerDataManager"
    override val priority = 10
    override val subPouvoir = Pouvoir

    private var playerDatabase: Database? = null

    private fun setupPlayerDatabase(conf: ConfigurationSection, table: String = conf.getString("table").toString()) {
        playerDatabase = Database(TypeSQL(HostSQL(conf), table))
    }

    private fun setupPlayerDatabase(
        host: String = "localhost",
        port: Int = 3306,
        user: String = "root",
        password: String = "root",
        database: String = "minecraft",
        table: String = "${pluginId.lowercase()}_database",
    ) {
        playerDatabase = Database(TypeSQL(HostSQL(host, port.toString(), user, password, database), table))
    }

    private fun setupPlayerDatabase(file: File) {
        playerDatabase = Database(TypeSQLite(file))
    }

    override fun onEnable() {
        if (Pouvoir.configManager["config"].getBoolean("database.enable")) {
            setupPlayerDatabase(Pouvoir.config.getConfigurationSection("database")!!)
        } else {
            setupPlayerDatabase(newFile(getDataFolder(), "data.db"))
        }
    }

    override operator fun get(user: String, key: String): String? {
        return Pouvoir.poolExecutor.submit<String?> { playerDatabase?.get(user, key) }.get()
    }

    override fun delete(user: String, key: String) {
        set(user, key, "null")
    }

    override operator fun set(user: String, key: String, value: String) {
        Pouvoir.poolExecutor.execute {
            playerDatabase?.set(user, key, value)
        }
    }

}