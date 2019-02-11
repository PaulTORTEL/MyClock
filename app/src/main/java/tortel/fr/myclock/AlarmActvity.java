package tortel.fr.myclock;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import tortel.fr.myclock.adapter.AlarmArrayAdapter;
import tortel.fr.myclock.bean.Alarm;
import tortel.fr.myclock.listener.DatabaseListener;
import tortel.fr.myclock.manager.DatabaseManager;
import tortel.fr.myclock.manager.SettingManager;
import tortel.fr.myclock.utils.AlarmUtils;

public class AlarmActvity extends AppCompatActivity implements DatabaseListener {

    private ListView listViewAlarm;
    private List<Alarm> listAlarm = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_actvity);

        LinearLayout mainLayout = findViewById(R.id.mainLayout);
        TextView title = findViewById(R.id.title);
        TextView subtitle = findViewById(R.id.subtitle);

        // Dark theme
        if (!SettingManager.getInstance().isInit()) {
            SettingManager.getInstance().init(this);
        }

        if ((boolean) SettingManager.getInstance().getPreferencesMap().get(getString(R.string.preferences_key_theme))) {
            mainLayout.setBackgroundColor(getResources().getColor(R.color.color_1));
            title.setTextColor(getResources().getColor(R.color.color_10));
            subtitle.setTextColor(getResources().getColor(R.color.color_6));
        } else {
            mainLayout.setBackgroundColor(getResources().getColor(R.color.color_8));
            title.setTextColor(Color.BLACK);
            subtitle.setTextColor(getResources().getColor(R.color.color_7));
        }

        listViewAlarm = findViewById(R.id.alarmList);
        registerForContextMenu(listViewAlarm);
        refreshAlarmList();
        loadAlarmList();
    }

    private void refreshAlarmList() {
        listViewAlarm.setAdapter(new AlarmArrayAdapter(getApplicationContext(), listAlarm));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(this, CreateAlarmActivity.class);
                startActivityForResult(intent, 0);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            if ("create".equals(data.getStringExtra("action"))) {
                Toast.makeText(this, "Your alarm has been created!", Toast.LENGTH_SHORT).show();
            } else if ("edit".equals(data.getStringExtra("action"))) {
                Toast.makeText(this, "Your alarm has been edited!", Toast.LENGTH_SHORT).show();
            }
            loadAlarmList();
        }
    }

    private void loadAlarmList() {
        DatabaseManager.getInstance().getAllAlarms(this, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.alarm_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onAlarmsFetched(final List<Alarm> listAlarm) {
        this.listAlarm = listAlarm;
        Collections.sort(this.listAlarm);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refreshAlarmList();
            }
        });
    }

    @Override
    public void onAlarmsSaved() {
        // NOTHING
    }

    @Override
    public void onAlarmDeleted() {
        Toast.makeText(this, "Your alarm has been deleted!", Toast.LENGTH_SHORT).show();
        refreshAlarmList();
    }

    @Override
    public void onAlarmUpdated() {
        // NOTHING
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.alarmList) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle("Alarm: " + listAlarm.get(info.position).getName());
            String[] menuItems = getResources().getStringArray(R.array.action_array);
            for (int i = 0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        Alarm alarm = listAlarm.get(index);
        switch (item.getItemId()) {
            case 0:
                Intent intent = new Intent(this, CreateAlarmActivity.class);
                intent.putExtra("alarm", alarm);
                startActivityForResult(intent, 0);
                break;

            case 1:
                AlarmUtils.createOrCancelAlarm(getApplicationContext(), alarm, false);
                DatabaseManager.getInstance().deleteAlarm(this, this, alarm);
                listAlarm.remove(index);
                break;
        }

        return super.onContextItemSelected(item);
    }
}
