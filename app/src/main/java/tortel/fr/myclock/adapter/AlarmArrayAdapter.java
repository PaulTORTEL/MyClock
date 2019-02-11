package tortel.fr.myclock.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import tortel.fr.myclock.R;
import tortel.fr.myclock.bean.Alarm;
import tortel.fr.myclock.listener.AlarmClickListener;
import tortel.fr.myclock.manager.SettingManager;

public class AlarmArrayAdapter extends ArrayAdapter<Alarm> {

    public AlarmArrayAdapter(@NonNull Context context, List<Alarm> alarmSpinnerList) {
        super(context, 0, alarmSpinnerList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View converView, ViewGroup parent) {
        if (converView == null) {
            converView = LayoutInflater.from(getContext()).inflate(R.layout.alarm_row, parent, false);
            converView.setLongClickable(true);
        }

        final ImageButton alarmButton = converView.findViewById(R.id.alarmButton);
        TextView alarmTv = converView.findViewById(R.id.alarmName);
        TextView dateTv = converView.findViewById(R.id.date);

        final Alarm currItem = getItem(position);

        if (currItem != null) {
            String name = currItem.getName();
            Calendar calendar = currItem.getAlarmTime();
            int min = calendar.get(Calendar.MINUTE);
            alarmTv.setText(name);
            dateTv.setText("Every day at " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + (min < 10 ? "0" + min : min));

            GradientDrawable shape = (GradientDrawable) getContext().getResources().getDrawable(R.drawable.roundcorner);

            // Dark theme
            boolean darkTheme = (boolean) SettingManager.getInstance().getPreferencesMap().get(getContext().getString(R.string.preferences_key_theme));

            if (currItem.isActivated()) {
                shape.setColor(getContext().getResources().getColor(R.color.color_4));
                alarmButton.setImageResource(R.drawable.alarm_on);

                if (darkTheme) {
                    alarmTv.setTextColor(getContext().getResources().getColor(R.color.color_8));
                } else {
                    alarmTv.setTextColor(Color.BLACK);
                }
            } else {
                shape.setColor(getContext().getResources().getColor(R.color.color_disabled_alarm));
                alarmButton.setImageResource(R.drawable.alarm_off);
                alarmTv.setTextColor(getContext().getResources().getColor(R.color.color_disabled_alarm));
            }

            TextView date = converView.findViewById(R.id.date);

            if (darkTheme) {
                date.setTextColor(getContext().getResources().getColor(R.color.color_11));
            } else {
                date.setTextColor(getContext().getResources().getColor(R.color.color_7));
            }

            alarmButton.setBackground(shape);
            alarmButton.setOnClickListener(new AlarmClickListener(currItem, alarmButton, getContext(), alarmTv));
        }

        return converView;
    }
}
