package me.iru.timedisplay.events

import me.iru.timedisplay.TimeDisplay
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.MinecraftClient

class EndClientTickHandler: ClientTickEvents.EndTick {
    override fun onEndTick(client: MinecraftClient?) {
        while(TimeDisplay.toggleKeyBinding.wasPressed()) {
            TimeDisplay.config.enabled = !TimeDisplay.config.enabled
        }
    }
}