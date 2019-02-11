package tortel.fr.myclock.bean;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Calendar;


@Entity
public class Alarm implements Comparable<Alarm>, Serializable {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "alarm_time")
    private Calendar alarmTime;

    @ColumnInfo(name = "is_activated")
    private boolean isActivated;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Calendar getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(Calendar alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
    }

    @Override
    public int compareTo(@NonNull Alarm alarm) {
        Calendar otherAlarmTime = alarm.getAlarmTime();

        if (alarmTime.get(Calendar.HOUR_OF_DAY) > otherAlarmTime.get(Calendar.HOUR_OF_DAY)) {
            return 1;
        }
        else if (alarmTime.get(Calendar.HOUR_OF_DAY) < otherAlarmTime.get(Calendar.HOUR_OF_DAY)) {
            return -1;
        }
        if (alarmTime.get(Calendar.MINUTE) > otherAlarmTime.get(Calendar.MINUTE)) {
            return 1;
        }
        else if (alarmTime.get(Calendar.MINUTE) < otherAlarmTime.get(Calendar.MINUTE)) {
            return -1;
        } else {
            return 0;
        }
    }
}
