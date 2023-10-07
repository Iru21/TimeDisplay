package me.iru.timedisplay

import me.iru.timedisplay.config.TimeDisplayConfig
import me.iru.timedisplay.events.ClientJoinHandler
import me.iru.timedisplay.events.EndClientTickHandler
import me.iru.timedisplay.events.EndWorldTickHandler
import me.iru.timedisplay.events.HudRenderHandler
import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.annotation.Config
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import org.lwjgl.glfw.GLFW

object TimeDisplay: ClientModInitializer {

    val offset = 7.5f
    val lines = mutableListOf<() -> String>()
    lateinit var toggleKeyBinding: KeyBinding

    var playtimeTickCache: Int = 0

    lateinit var config: TimeDisplayConfig

    override fun onInitializeClient() {
        println("[Time Display] Enabling...")

        AutoConfig.register(
            TimeDisplayConfig::class.java
        ) { definition: Config?, configClass: Class<TimeDisplayConfig?>? ->
            JanksonConfigSerializer(
                definition,
                configClass
            )
        }

        this.config = AutoConfig.getConfigHolder(TimeDisplayConfig::class.java).get()

        this.toggleKeyBinding = KeyBindingHelper.registerKeyBinding(
            KeyBinding(
                "key.timedisplay.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F9,
                "category.timedisplay.keybinds"
            )
        )

        lines.add(TimeUtils.getMinecraftHour)
        lines.add(TimeUtils.getMinecraftTotalDays)
        lines.add(TimeUtils.getMinecraftDate)
        lines.add(TimeUtils.getPlayTime)
        lines.add(TimeUtils.getRealDate)
        lines.add(TimeUtils.getRealTime)

        HudRenderCallback.EVENT.register(HudRenderHandler())
        ClientPlayConnectionEvents.JOIN.register(ClientJoinHandler())
        ClientTickEvents.END_WORLD_TICK.register(EndWorldTickHandler())
        ClientTickEvents.END_CLIENT_TICK.register(EndClientTickHandler())
    }
}