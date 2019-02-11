package tortel.fr.myclock.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import tortel.fr.myclock.bean.Alarm;
import tortel.fr.myclock.receiver.AlarmReceiver;

public class AlarmUtils {
    public static void createOrCancelAlarm(Context context, Alarm alarm, boolean isCreating) {

        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);

        alarmIntent.putExtra("alarmId", alarm.getId());
        alarmIntent.putExtra("alarmName", alarm.getName());
        alarmIntent.putExtra("alarmHour", alarm.getAlarmTime().get(Calendar.HOUR_OF_DAY));
        alarmIntent.putExtra("alarmMinute", alarm.getAlarmTime().get(Calendar.MINUTE));
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, alarm.getId(), alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (isCreating) {
            Calendar nextAlarmDate = getNextAlarmTime(alarm.getAlarmTime());
            alarmMgr.setExact(AlarmManager.RTC_WAKEUP, nextAlarmDate.getTimeInMillis(), alarmPendingIntent);
        } else {
            alarmMgr.cancel(alarmPendingIntent);
        }
    }

    private static Calendar getNextAlarmTime(Calendar alarmTime) {
        Calendar now = Calendar.getInstance();

        int alarmMin = alarmTime.get(Calendar.MINUTE);
        int alarmHour = alarmTime.get(Calendar.HOUR_OF_DAY);

        Calendar nextAlarmDate = Calendar.getInstance();
        nextAlarmDate.setTimeInMillis(System.currentTimeMillis());
        nextAlarmDate.set(Calendar.HOUR_OF_DAY, alarmHour);
        nextAlarmDate.set(Calendar.MINUTE, alarmMin);
        nextAlarmDate.set(Calendar.SECOND, 0);
        nextAlarmDate.set(Calendar.MILLISECOND, 0);

        // Timing has passed
        if (nextAlarmDate.getTimeInMillis() <= now.getTimeInMillis()) {
            // We setup the calendar as the date of tomorrow
            nextAlarmDate.add(Calendar.DAY_OF_MONTH, 1);
        }

        return nextAlarmDate;
    }
}
