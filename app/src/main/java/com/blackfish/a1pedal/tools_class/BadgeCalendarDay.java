package com.blackfish.a1pedal.tools_class;

import com.prolificinteractive.materialcalendarview.CalendarDay;

public class BadgeCalendarDay {
    String eventFromOneDay;

    public String getEventFromOneDay() {
        return eventFromOneDay;
    }

    public void setEventFromOneDay(String eventFromOneDay) {
        this.eventFromOneDay = eventFromOneDay;
    }

    public com.prolificinteractive.materialcalendarview.CalendarDay getCalendarDay() {
        return CalendarDay;
    }

    public void setCalendarDay(com.prolificinteractive.materialcalendarview.CalendarDay calendarDay) {
        CalendarDay = calendarDay;
    }

    public BadgeCalendarDay(String eventFromOneDay, com.prolificinteractive.materialcalendarview.CalendarDay calendarDay) {
        this.eventFromOneDay = eventFromOneDay;
        CalendarDay = calendarDay;
    }

    CalendarDay CalendarDay;
}
