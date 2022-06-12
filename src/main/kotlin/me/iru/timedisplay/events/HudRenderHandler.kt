package me.iru.timedisplay.events

import me.iru.timedisplay.TimeDisplay
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import java.util.*

class HudRenderHandler : HudRenderCallback {
    override fun onHudRender(matrixStack: MatrixStack?, tickDelta: Float) {
        val mc = MinecraftClient.getInstance()
        if(matrixStack != null && !mc.options.debugEnabled) {
            var linesData: List<String> = TimeDisplay.lines.toList().map { it() }
            Collections.sort(linesData, Comparator.comparing(String::length))
            linesData = linesData.reversed()
            var posY = TimeDisplay.offset
            val height = mc.textRenderer.fontHeight + 2
            for (i in 1.. linesData.size) {
                val line = style(linesData[i - 1])
                val width = mc.textRenderer.getWidth(line)
                DrawableHelper.fill(
                    matrixStack,
                    (TimeDisplay.offset - 1).toInt(),
                    (posY - 1).toInt(),
                    width + 8 + (if((width + 8) < 100) 1 else 0),
                    (posY + mc.textRenderer.fontHeight + 1).toInt(),
                    (0x40000000).toInt()
                )
                mc.textRenderer.drawWithShadow(
                    matrixStack,
                    line,
                    TimeDisplay.offset,
                    posY,
                    0xFFFFFF
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
                    it.withColor(0xffaa00)
                } else if(ch == ':' || ch == '/' || ch == '(' || ch == ')') {
                    it.withColor(0xaaaaaa)
                } else {
                    it
                }
            })
            r.append("§r")
        }
        return r
    }

}