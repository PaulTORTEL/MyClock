package tortel.fr.myclock.listener;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import tortel.fr.myclock.R;
import tortel.fr.myclock.bean.Alarm;
import tortel.fr.myclock.manager.DatabaseManager;
import tortel.fr.myclock.manager.SettingManager;
import tortel.fr.myclock.utils.AlarmUtils;

public class AlarmClickListener implements View.OnClickListener {

    private Alarm alarm;
    private ImageButton button;
    private Context context;
    private TextView alarmTv;

    public AlarmClickListener(Alarm alarm, ImageButton button, Context context, TextView alarmTv) {
        this.alarm = alarm;
        this.button = button;
        this.context = context;
        this.alarmTv = alarmTv;
    }

    @Override
    public void onClick(View view) {

        // Dark theme
        boolean darkTheme = (boolean) SettingManager.getInstance().getPreferencesMap().get(context.getString(R.string.preferences_key_theme));

        alarm.setActivated(!alarm.isActivated());

        GradientDrawable shape = (GradientDrawable) context.getResources().getDrawable(R.drawable.roundcorner);
        if (alarm.isActivated()) {
            shape.setColor(context.getResources().getColor(R.color.color_4));
            if (darkTheme) {
                alarmTv.setTextColor(context.getResources().getColor(R.color.color_8));
            } else {
                alarmTv.setTextColor(Color.BLACK);
            }
        } else {
            shape.setColor(context.getResources().getColor(R.color.color_disabled_alarm));
            alarmTv.setTextColor(context.getResources().getColor(R.color.color_disabled_alarm));
        }

        button.setBackground(shape);

        if (alarm.isActivated()) {
            AlarmUtils.createOrCancelAlarm(context, alarm, true);
            button.setImageResource(R.drawable.alarm_on);
            Toast.makeText(context, "Alarm \"" + alarm.getName() + "\" activated", Toast.LENGTH_SHORT).show();
        } else {
            AlarmUtils.createOrCancelAlarm(context, alarm, false);
            button.setImageResource(R.drawable.alarm_off);
        }

        DatabaseManager.getInstance().updateAlarms(null, context, alarm);
    }
}
