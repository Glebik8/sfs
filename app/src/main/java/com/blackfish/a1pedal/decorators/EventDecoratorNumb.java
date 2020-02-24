package com.blackfish.a1pedal.decorators;

import android.widget.TextView;

import com.blackfish.a1pedal.tools_class.BadgeCalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;
import com.blackfish.a1pedal.decorators.CalendarViewSpan;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class EventDecoratorNumb implements DayViewDecorator {

    private int color;
    private CalendarDay days;
    private ArrayList <BadgeCalendarDay> badgeCalendarDays ;
    private String st ;

    public EventDecoratorNumb(ArrayList<BadgeCalendarDay> badgeCalendarDayss) {
        this.badgeCalendarDays = badgeCalendarDayss;

    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        this.days = day;
        ArrayList<CalendarDay> calendarDays = new ArrayList<>();
        for (int i = 0 ; i<badgeCalendarDays.size() ; i++)
        {
            calendarDays.add(badgeCalendarDays.get(i).getCalendarDay());
        }

        return calendarDays.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view ) {

        String event = "" ;
        for (int i = 0 ; i<badgeCalendarDays.size() ; i++)
        {
         if ( badgeCalendarDays.get(i).getCalendarDay().equals(days))
         {event= badgeCalendarDays.get(i).getEventFromOneDay();}
        }

        view.addSpan(new CalendarViewSpan(event));
    }
}

