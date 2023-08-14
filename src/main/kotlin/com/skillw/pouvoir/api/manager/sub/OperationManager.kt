package com.skillw.pouvoir.api.manager.sub

import com.skillw.pouvoir.api.feature.operation.Operation
import com.skillw.pouvoir.api.manager.Manager
import com.skillw.pouvoir.api.plugin.map.LowerKeyMap

/**
 * NumberOperation manager
 *
 * @constructor Create empty NumberOperation manager
 */
abstract class OperationManager : LowerKeyMap<Operation<*>>(), Manager
