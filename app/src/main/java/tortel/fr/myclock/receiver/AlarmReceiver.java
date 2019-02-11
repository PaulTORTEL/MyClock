package tortel.fr.myclock.receiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import tortel.fr.myclock.AlarmActvity;
import tortel.fr.myclock.R;
import tortel.fr.myclock.bean.Alarm;
import tortel.fr.myclock.utils.AlarmUtils;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive (Context context, Intent intent) {

        int alarmId = intent.getIntExtra("alarmId", -1);
        String alarmName = intent.getStringExtra("alarmName");
        int alarmHour = intent.getIntExtra("alarmHour", 0);
        int alarmMinute = intent.getIntExtra("alarmMinute", 0);

        // For our recurring task, we'll just display a message
        Toast.makeText(context, "Your alarm [ " + alarmName + " ] is ringing", Toast.LENGTH_LONG).show();
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        long[] pattern = {0, 1000, 500, 400, 250, 400, 250, 1000};
        vibrator.vibrate(pattern, -1);
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        final Ringtone r = RingtoneManager.getRingtone(context, notification);
        if (r != null)
            r.play();

        // The channel ID of the notification.
        String id = "my-clock-channel";
        // Build intent for notification content
        Intent viewIntent = new Intent(context, AlarmActvity.class);
        viewIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        //PendingIntent viewPendingIntent = PendingIntent.getActivity(context, 0, viewIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Using taskStackBuilder to manage the stack of the app
        PendingIntent viewPendingIntent = TaskStackBuilder.create(context).addNextIntentWithParentStack(viewIntent).getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get an instance of the NotificationManager service
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My Clock channel";
            String description = "Channel for My Clock app";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel mChannel = new NotificationChannel(id, name, importance);
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.BLUE);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, id)
                        .setSmallIcon(R.drawable.alarm)
                        .setContentTitle(alarmName + " rang")
                        .setContentText("The alarm " + alarmName + " rang at " + alarmHour + ":" + (alarmMinute < 10 ? "0" + alarmMinute : alarmMinute))
                        .extend(new NotificationCompat.WearableExtender().setHintShowBackgroundOnly(true))
                        .setContentIntent(viewPendingIntent)
                        .addAction(R.drawable.exit_to_app,
                                "Check your alarms", viewPendingIntent)
                        .setAutoCancel(true);

        // Issue the notification with notification manager.
        notificationManager.notify(alarmId, notificationBuilder.build());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, alarmHour);
        calendar.set(Calendar.MINUTE, alarmMinute);
        calendar.set(Calendar.SECOND, 0);

        Alarm alarm = new Alarm();
        alarm.setId(alarmId);
        alarm.setName(alarmName);
        alarm.setAlarmTime(calendar);
        AlarmUtils.createOrCancelAlarm(context, alarm, true);

        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                if (r != null)
                    r.stop();
                t.cancel();
            }
        }, 5000);

    }
}
