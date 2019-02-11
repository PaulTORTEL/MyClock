package tortel.fr.myclock.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import tortel.fr.myclock.bean.Alarm;
import tortel.fr.myclock.listener.DatabaseListener;
import tortel.fr.myclock.manager.DatabaseManager;
import tortel.fr.myclock.utils.AlarmUtils;

public class BootReceiver extends BroadcastReceiver implements DatabaseListener {
    private Context context;
    private Handler handler;

    @Override
    public void onReceive(final Context context, Intent intent) {
        this.context = context;
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            handler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message message) {
                    int alarmNb = (int) message.obj;
                    Toast.makeText(BootReceiver.this.context, "Your " + alarmNb + " alarms have been re-applied", Toast.LENGTH_LONG).show();
                }
            };
            DatabaseManager.getInstance().getAllAlarms(this, context);
        }
    }

    @Override
    public void onAlarmsFetched(List<Alarm> listAlarm) {
        for (Alarm alarm : listAlarm) {
            if (alarm.isActivated()) {
                AlarmUtils.createOrCancelAlarm(context, alarm, true);
            }
        }
        Message message = handler.obtainMessage(1, listAlarm.size());
        message.sendToTarget();
    }

    @Override
    public void onAlarmsSaved() {
        // NOTHING
    }

    @Override
    public void onAlarmDeleted() {
        // NOTHING
    }

    @Override
    public void onAlarmUpdated() {
        // NOTHING
    }
}
