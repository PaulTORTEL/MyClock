package tortel.fr.myclock;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;

import tortel.fr.myclock.drawing.ClockSurfaceView;
import tortel.fr.myclock.manager.SettingManager;


public class MainActivity extends AppCompatActivity {

    private ClockSurfaceView clockSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        clockSurfaceView = new ClockSurfaceView(this, width - (0.6f * width), this);
        setContentView(clockSurfaceView);

        SettingManager.getInstance().init(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        clockSurfaceView.onClockResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        clockSurfaceView.onClockPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_alarm:
                Intent intentAlarm = new Intent(this, AlarmActvity.class);
                startActivity(intentAlarm);
                return true;

            case R.id.action_preferences:
                Intent intentPref = new Intent(this, PreferencesActivity.class);
                startActivity(intentPref);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
