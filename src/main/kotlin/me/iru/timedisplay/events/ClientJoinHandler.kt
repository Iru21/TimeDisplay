package me.iru.timedisplay.events

import me.iru.timedisplay.TimeDisplay
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.network.packet.c2s.play.ClientStatusC2SPacket
import net.minecraft.stat.Stats

class ClientJoinHandler: ClientPlayConnectionEvents.Join {
    override fun onPlayReady(handler: ClientPlayNetworkHandler?, sender: PacketSender?, client: MinecraftClient?) {
        val player = MinecraftClient.getInstance().player!!
        player.networkHandler.sendPacket(ClientStatusC2SPacket(ClientStatusC2SPacket.Mode.REQUEST_STATS))
        Thread {
            Thread.sleep(5000)
            client!!.execute {
                TimeDisplay.playtimeTickCache = MinecraftClient.getInstance().player!!.statHandler.getStat(
                    Stats.CUSTOM.getOrCreateStat(
                        Stats.PLAY_TIME
                    )
                )
            }
        }.start()
    }
}