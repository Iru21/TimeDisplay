package me.iru.timedisplay.events

import me.iru.timedisplay.TimeDisplay
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text
import java.util.*

class HudRenderHandler : HudRenderCallback {
    override fun onHudRender(drawContext: DrawContext?, tickDelta: Float) {
        val mc = MinecraftClient.getInstance()
        if (drawContext != null && !mc.options.debugEnabled && TimeDisplay.config.enabled) {
            var linesData: List<String> = TimeDisplay.lines.toList().map { it() }

            if (TimeDisplay.config.sortLinesByLength) {
                Collections.sort(linesData, Comparator.comparing(String::length))
                linesData = linesData.reversed()
            }

            var posY = TimeDisplay.offset
            val height = mc.textRenderer.fontHeight + 2
            for (i in 1..linesData.size) {
                val line = style(linesData[i - 1])
                val width = mc.textRenderer.getWidth(line)
                drawContext.fill(
                    (TimeDisplay.offset - 1).toInt(),
                    (posY - 1).toInt(),
                    width + 8 + (if ((width + 8) < 100) 1 else 0),
                    (posY + mc.textRenderer.fontHeight + 1).toInt(),
                    (0x40000000).toInt()
                )
                drawContext.drawTextWithShadow(
                    mc.textRenderer,
                    line,
                    TimeDisplay.offset.toInt(),
                    posY.toInt(),
                    TimeDisplay.config.primaryColor
                )
                posY += height
            }
        }
    }

    private fun style(s: String): Text {
        val r = Text.literal("")
        for(ch in s) {
            r.append(Text.literal(ch.toString()).styled {
                if(ch.isDigit()) {
                    it.withColor(TimeDisplay.config.accentColor)
                } else if(ch == ':' || ch == '/' || ch == '(' || ch == ')') {
                    it.withColor(TimeDisplay.config.secondaryColor)
                } else {
                    it
                }
            })
            r.append("§r")
        }
        return r
    }

}