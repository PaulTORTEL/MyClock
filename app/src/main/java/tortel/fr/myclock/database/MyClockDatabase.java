package tortel.fr.myclock.database;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import tortel.fr.myclock.bean.Alarm;
import tortel.fr.myclock.converter.CalendarConverter;
import tortel.fr.myclock.database.dao.AlarmDao;

@Database(entities = {Alarm.class}, version = 2)
@TypeConverters({CalendarConverter.class})
public abstract class MyClockDatabase extends RoomDatabase {
    public abstract AlarmDao alarmDao();
}
