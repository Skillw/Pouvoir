package com.skillw.pouvoir.internal.feature.database.sql.sqlite

import com.skillw.pouvoir.api.feature.database.DatabaseType
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import com.skillw.pouvoir.api.plugin.map.DataMap
import com.skillw.pouvoir.internal.feature.database.sql.NormalContainer
import taboolib.common.io.newFile
import taboolib.module.database.Host
import taboolib.module.database.HostSQLite
import taboolib.module.database.SQLite
import java.io.File

@AutoRegister
internal object SQLite : DatabaseType<SQLiteContainerHolder, NormalContainer<Host<SQLite>, SQLite>>("sqlite") {

    override fun connect(params: DataMap): SQLiteContainerHolder {
        val path = params.get("path", "plugins/Pouvoir/database.db")
        val syncTime = params.get("user-container-sync-time", 360000L)
        val dataFile = newFile(File(path))
        val host = HostSQLite(dataFile)
        return SQLiteContainerHolder(host).apply { userContainerSyncTime = syncTime }
    }
}