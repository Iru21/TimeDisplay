package me.iru.timedisplay.config

import me.shedaniel.autoconfig.ConfigData
import me.shedaniel.autoconfig.annotation.Config
import me.shedaniel.autoconfig.annotation.ConfigEntry

@Config(name = "timedisplay")
class TimeDisplayConfig : ConfigData {
    var enabled: Boolean = true

    var twelveClockMode: Boolean = false

    var sortLinesByLength: Boolean = false

    @ConfigEntry.ColorPicker
    var primaryColor: Int = 0xffffff

    @ConfigEntry.ColorPicker
    var secondaryColor: Int = 0xaaaaaa

    @ConfigEntry.ColorPicker
    var accentColor: Int = 0xffaa00
}