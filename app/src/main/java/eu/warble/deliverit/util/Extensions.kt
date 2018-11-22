package eu.warble.deliverit.util

import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId

fun LocalDateTime.toLongMillis(): Long {
    return atZone(ZoneId.of("Europe/Warsaw")).toInstant().toEpochMilli()
}