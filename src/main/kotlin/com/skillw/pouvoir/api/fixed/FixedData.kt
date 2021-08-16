package com.skillw.pouvoir.api.fixed

import com.skillw.pouvoir.Pouvoir.configManager
import taboolib.library.configuration.ConfigurationSection

class FixedData {
    var integerMin: Int
    var integerMax: Int
    var decimalMin: Int
    var decimalMax: Int
    var decimalValue: Int

    constructor(integerMin: Int, integerMax: Int, decimalMin: Int, decimalMax: Int, decimalValue: Int) {
        this.integerMin = integerMin
        this.integerMax = integerMax
        this.decimalMin = decimalMin
        this.decimalMax = decimalMax
        this.decimalValue = decimalValue
    }

    constructor(section: ConfigurationSection) {
        integerMin = section.getInt("integer.min")
        integerMax = section.getInt("integer.max")
        decimalMin = section.getInt("decimal.min")
        decimalMax = section.getInt("decimal.max")
        decimalValue = section.getInt("decimal.value")
    }

    companion object {
        var defaultData: FixedData? = null
        fun load() {
            defaultData = FixedData(configManager["config"].getConfigurationSection("options.fixed"))
        }
    }
}