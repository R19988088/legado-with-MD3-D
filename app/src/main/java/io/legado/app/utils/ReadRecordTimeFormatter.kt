package io.legado.app.utils

fun formatReadDuration(millis: Long): String {
    val days = millis / (1000 * 60 * 60 * 24)
    val hours = millis % (1000 * 60 * 60 * 24) / (1000 * 60 * 60)
    val minutes = millis % (1000 * 60 * 60) / (1000 * 60)
    val seconds = millis % (1000 * 60) / 1000
    val d = if (days > 0) "${days}天" else ""
    val h = if (hours > 0) "${hours}小时" else ""
    val m = if (minutes > 0) "${minutes}分钟" else ""
    val s = if (seconds > 0) "${seconds}秒" else ""
    return if ("$d$h$m$s".isBlank()) "0秒" else "$d$h$m$s"
}

fun formatBookInfoReadDuration(millis: Long): String {
    val safeMillis = millis.coerceAtLeast(0L)
    val totalSeconds = safeMillis / 1000
    val totalMinutes = safeMillis / (1000 * 60)
    val totalHours = safeMillis / (1000.0 * 60 * 60)
    val duration = when {
        safeMillis >= 1000L * 60 * 60 -> "${formatOneDecimal(totalHours)}小时"
        safeMillis >= 1000L * 60 -> "${totalMinutes}分钟"
        else -> "${totalSeconds}秒"
    }
    return "已经阅读 $duration"
}

private fun formatOneDecimal(value: Double): String {
    val rounded = kotlin.math.round(value * 10) / 10
    return if (rounded % 1.0 == 0.0) {
        rounded.toInt().toString()
    } else {
        rounded.toString()
    }
}
