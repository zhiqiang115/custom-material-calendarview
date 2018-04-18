package com.prolificinteractive.materialcalendarview;

import java.util.Calendar;
import java.util.List;

/**
 * Created by cuonghc on 4/10/18.
 */

public class CustomMonth {
    private CalendarDay mCalendarDay;
    private List<CustomDay> mCustomDays;

    public CustomMonth(CalendarDay calendarDay, List<CustomDay> customDays) {
        mCalendarDay = calendarDay;
        mCustomDays = customDays;
    }

    public CalendarDay getCalendarDay() {
        return mCalendarDay;
    }

    public void setCalendarDay(CalendarDay calendarDay) {
        mCalendarDay = calendarDay;
    }

    public List<CustomDay> getCustomDays() {
        return mCustomDays;
    }

    public void setCustomDays(List<CustomDay> customDays) {
        mCustomDays = customDays;
    }
}
