package me.iru.timedisplay

import me.iru.timedisplay.events.ClientJoinHandler
import me.iru.timedisplay.events.EndWorldTickHandler
import me.iru.timedisplay.events.HudRenderHandler
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import org.lwjgl.glfw.GLFW

object TimeDisplay: ModInitializer {

    private const val modName = "Time Display"
    private const val version = "1.1a"

    val offset = 7.5f
    val lines = mutableListOf<() -> String>()

    var playtimeTickCache: Int = 0

    override fun onInitialize() {
        println("[$modName] Enabling $version")

        lines.add(TimeUtils.getMinecraftHour)
        lines.add(TimeUtils.getMinecraftTotalDays)
        lines.add(TimeUtils.getMinecraftDate)
        lines.add(TimeUtils.getPlayTime)
        lines.add(TimeUtils.getRealDate)
        lines.add(TimeUtils.getRealTime)

        HudRenderCallback.EVENT.register(HudRenderHandler())
        ClientPlayConnectionEvents.JOIN.register(ClientJoinHandler())
        ClientTickEvents.END_WORLD_TICK.register(EndWorldTickHandler())
    }
}