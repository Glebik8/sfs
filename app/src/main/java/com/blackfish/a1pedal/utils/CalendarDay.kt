package com.blackfish.a1pedal.utils

import com.prolificinteractive.materialcalendarview.CalendarDay

fun correctDate(calendarDay: CalendarDay): String {
    return buildString {
        if (calendarDay.day < 10)
            append('0')
        append("${calendarDay.day}/")
        if (calendarDay.month < 10)
            append('0')
        append("${calendarDay.month}/${calendarDay.year}")
    }
}

fun String.toCalendarDay(): CalendarDay {
    // "12/18/2020"
    val values = split('/').map(String::toInt)
    return CalendarDay.from(values[2], values[1], values[0])
}

