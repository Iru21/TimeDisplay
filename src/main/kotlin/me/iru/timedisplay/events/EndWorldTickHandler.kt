package me.iru.timedisplay.events

import me.iru.timedisplay.TimeDisplay
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.world.ClientWorld


class EndWorldTickHandler: ClientTickEvents.EndWorldTick {
    override fun onEndTick(world: ClientWorld?) {
        TimeDisplay.playtimeTickCache++
    }
}