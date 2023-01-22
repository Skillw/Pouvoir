package com.skillw.pouvoir.internal.feature.database.sql.sql

import com.skillw.pouvoir.api.feature.database.DatabaseType
import com.skillw.pouvoir.api.plugin.annotation.AutoRegister
import com.skillw.pouvoir.api.plugin.map.DataMap
import com.skillw.pouvoir.internal.feature.database.sql.NormalContainer
import taboolib.module.database.Host
import taboolib.module.database.HostSQL
import taboolib.module.database.SQL

@AutoRegister
internal object SQL : DatabaseType<SQLContainerHolder, NormalContainer<Host<SQL>, SQL>>("mysql") {
    override fun connect(params: DataMap): SQLContainerHolder {
        val hostStr = params.get("host", "localhost")
        val port = params.get("port", "3306")
        val user = params.get("user", "root")
        val password = params.get("password", "root")
        val database = params.get("database", "root")
        val syncTime = params.get("user-container-sync-time", 360000L)
        val host = HostSQL(hostStr, port, user, password, database)
        return SQLContainerHolder(host).apply { userContainerSyncTime = syncTime }
    }
}