package me.iru.timedisplay.config

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import me.shedaniel.autoconfig.AutoConfig
import net.minecraft.client.gui.screen.Screen

class ModMenuIntegration: ModMenuApi {
    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> {
        return ConfigScreenFactory { parent: Screen? ->
            AutoConfig.getConfigScreen(
                TimeDisplayConfig::class.java, parent
            ).get()
        }
    }

}