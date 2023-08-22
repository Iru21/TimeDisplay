package me.iru.timedisplay

import net.minecraft.client.MinecraftClient
import java.text.SimpleDateFormat
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.TimeUnit

object TimeUtils {

    val getPlayTime: () -> String = {
        val playtime = TimeDisplay.playtimeTickCache
        val playtimeInSecons = playtime / 20
        val hour = (playtimeInSecons / 60 / 60)
        val min = (playtimeInSecons / 60) % 60
        val sec = (playtimeInSecons) % 60
        val formatted = "${String.format("%02d", hour)}:${String.format("%02d", min)}:${String.format("%02d", sec)}"
        "Play Time: ($formatted)"
    }

    val getRealTime: () -> String = {
        val format = if (TimeDisplay.config.twelveClockMode) "h:mm:ss a" else "HH:mm:ss"
        val sdf = SimpleDateFormat(format)
        sdf.format(System.currentTimeMillis())
    }

    val getRealDate: () -> String = {
        val sdf = SimpleDateFormat("yyyy/MM/dd")
        sdf.format(System.currentTimeMillis())
    }

    val getMinecraftHour: () -> String = {
        val timeDay: Long = MinecraftClient.getInstance().world!!.timeOfDay
        val dayTicks = (timeDay % 24000).toInt()
        val hour = (dayTicks / 1000 + 6) % 24
        val min = (dayTicks / 16.666666).toInt() % 60
        val sec = (dayTicks / 0.277777).toInt() % 60

        "Ingame Time (${String.format("%02d", hour)}:${String.format("%02d", min)}:${String.format("%02d", sec)})"
    }

    val getMinecraftDate: () -> String = {
        val timeDay: Long = MinecraftClient.getInstance().world!!.timeOfDay
        val days = (timeDay / 24000L)
        val start = Date(31530000000).toInstant()
        val now = Date(TimeUnit.DAYS.toMillis(days)).toInstant()
        val sdf = SimpleDateFormat("dd MMMM '(Year' yyyy')'", Locale.ENGLISH)
        val formatted = sdf.format(ChronoUnit.MILLIS.between(start, now))
        val pieces = formatted.split("(Year ")
        val year = pieces[1].removeSuffix(")").toInt() - 1968

        "${pieces[0]}(Year ${year})"
    }

    val getMinecraftTotalDays: () -> String = {
        val timeDay: Long = MinecraftClient.getInstance().world!!.timeOfDay
        "Total Ingame Days (${timeDay / 24000L})"
    }
}