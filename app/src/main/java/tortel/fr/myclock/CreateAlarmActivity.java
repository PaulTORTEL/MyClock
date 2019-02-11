package tortel.fr.myclock;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import tortel.fr.myclock.bean.Alarm;
import tortel.fr.myclock.listener.DatabaseListener;
import tortel.fr.myclock.manager.DatabaseManager;
import tortel.fr.myclock.manager.SettingManager;
import tortel.fr.myclock.utils.AlarmUtils;

public class CreateAlarmActivity extends AppCompatActivity implements DatabaseListener {

    private Calendar calendar;
    private EditText timeAlarmTv;
    private EditText nameAlarmTv;

    private Alarm alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_alarm);

        nameAlarmTv = findViewById(R.id.nameAlarm);
        // We request focus for the title Textview
        nameAlarmTv.requestFocus();
        // We display the virtual keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        timeAlarmTv = findViewById(R.id.timeAlarm);
        timeAlarmTv.setKeyListener(null);

        Button createBtn = findViewById(R.id.createAlarmBtn);
        Button editBtn = findViewById(R.id.editAlarmBtn);

        alarm = (Alarm) getIntent().getSerializableExtra("alarm");
        TextView title = findViewById(R.id.title);
        // Edition
        if (alarm != null) {
            title.setText(R.string.alarm_creation_title_edit);
            editBtn.setVisibility(View.VISIBLE);
            createBtn.setVisibility(View.GONE);
            calendar = alarm.getAlarmTime();
            nameAlarmTv.setText(alarm.getName());
            updateTimeAlarm();
        } else {
            // Creation
            title.setText(R.string.alarm_creation_title);
            editBtn.setVisibility(View.GONE);
            createBtn.setVisibility(View.VISIBLE);
            calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
        }

        // We setup the listener of the datePickerDialog
        final TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {

            public void onTimeSet(TimePicker view, int hour, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                updateTimeAlarm();
            }
        };

        timeAlarmTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(CreateAlarmActivity.this, timeSetListener,
                        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                        true).show();
            }
        });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               if (!isFormFilled())
                   return;

                int randomInt = new Random().nextInt(1000000);
                Alarm alarm = new Alarm();
                alarm.setId(randomInt);
                alarm.setAlarmTime(calendar);
                alarm.setName(nameAlarmTv.getText().toString());
                alarm.setActivated(true);

                AlarmUtils.createOrCancelAlarm(getApplicationContext(), alarm, true);

                DatabaseManager.getInstance().insertAlarms(CreateAlarmActivity.this, getApplicationContext(), alarm);
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFormFilled())
                    return;

                alarm.setName(nameAlarmTv.getText().toString());
                if (alarm.isActivated()) {
                    AlarmUtils.createOrCancelAlarm(getApplicationContext(), alarm, true);
                }
                DatabaseManager.getInstance().updateAlarms(CreateAlarmActivity.this, getApplicationContext(), alarm);
            }
        });

        LinearLayout mainLayout = findViewById(R.id.mainLayout);
        TextView nameTv = findViewById(R.id.name);
        // Dark theme
        boolean darkTheme = (boolean) SettingManager.getInstance().getPreferencesMap().get(getString(R.string.preferences_key_theme));

        if (darkTheme) {
            mainLayout.setBackgroundColor(getResources().getColor(R.color.color_1));
            title.setTextColor(getResources().getColor(R.color.color_10));
            nameAlarmTv.setTextColor(getResources().getColor(R.color.color_8));
            nameTv.setTextColor(getResources().getColor(R.color.color_6));
            timeAlarmTv.setTextColor(getResources().getColor(R.color.color_8));
            timeAlarmTv.getBackground().setColorFilter(getResources().getColor(R.color.color_8), PorterDuff.Mode.SRC_IN);
            timeAlarmTv.setHintTextColor(getResources().getColor(R.color.color_disabled_alarm));
            nameAlarmTv.setHintTextColor(getResources().getColor(R.color.color_disabled_alarm));
        } else {
            mainLayout.setBackgroundColor(getResources().getColor(R.color.color_8));
            title.setTextColor(Color.BLACK);
            nameAlarmTv.setTextColor(Color.BLACK);
            nameAlarmTv.getBackground().clearColorFilter();
            timeAlarmTv.getBackground().clearColorFilter();
            timeAlarmTv.setHintTextColor(Color.GRAY);
            nameAlarmTv.setHintTextColor(Color.GRAY);
        }
    }

    private boolean isFormFilled() {
        if (nameAlarmTv.getText().toString().isEmpty()) {
            Toast.makeText(CreateAlarmActivity.this, "Please, enter a name for your alarm", Toast.LENGTH_SHORT).show();
            return false;
        } else if (timeAlarmTv.getText().toString().isEmpty()) {
            Toast.makeText(CreateAlarmActivity.this, "Please, enter a time for your alarm", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void updateTimeAlarm() {
        int min = calendar.get(Calendar.MINUTE);
        timeAlarmTv.setText(calendar.get(Calendar.HOUR_OF_DAY) + ":" +  (min < 10 ? "0" + min : min));
    }

    @Override
    public void onAlarmsSaved() {
        Intent intent = new Intent();
        intent.putExtra("action", "create");
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onAlarmsFetched(List<Alarm> listAlarm) {
        // NOTHING
    }

    @Override
    public void onAlarmDeleted() {
        // NOTHING
    }

    @Override
    public void onAlarmUpdated() {
        Intent intent = new Intent();
        intent.putExtra("action", "edit");
        setResult(RESULT_OK, intent);
        finish();
    }
}
