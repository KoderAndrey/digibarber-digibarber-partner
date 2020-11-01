package com.digibarber.app.materialcalendarview.format;

import androidx.annotation.NonNull;

import com.digibarber.app.materialcalendarview.CalendarDay;

import java.text.SimpleDateFormat;

/**
 * Supply labels for a given day. Default implementation is to format using a {@linkplain SimpleDateFormat}
 */
public interface DayFormatter {

    /**
     * Format a given day into a string
     *
     * @param day the day
     * @return a label for the day
     */
    @NonNull
    String format(@NonNull CalendarDay day);

    /**
     * Default implementation used by {@linkplain com.digibarber.app.materialcalendarview.MaterialCalendarView}
     */
    public static final com.digibarber.app.materialcalendarview.format.DayFormatter DEFAULT = new DateFormatDayFormatter();
}
