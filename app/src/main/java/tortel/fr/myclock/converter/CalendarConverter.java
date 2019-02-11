package tortel.fr.myclock.converter;

import android.arch.persistence.room.TypeConverter;

import java.util.Calendar;


public class CalendarConverter {
    @TypeConverter
    public Calendar fromTimestamp(Long value) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(value);
        return value == null ? null : cal;
    }

    @TypeConverter
    public static Long dateToTimestamp(Calendar calendar) {
        return calendar == null ? null : calendar.getTimeInMillis();
    }
}