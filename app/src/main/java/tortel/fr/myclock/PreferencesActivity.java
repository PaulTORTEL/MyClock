package tortel.fr.myclock;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import tortel.fr.myclock.adapter.ColorSpinnerAdapter;
import tortel.fr.myclock.manager.SettingManager;

public class PreferencesActivity extends AppCompatActivity {

    private Spinner spinnerColor;
    private Spinner spinnerColor2;
    private Spinner spinnerColor3;
    private Spinner spinnerColor4;
    private Spinner spinnerColor5;
    private Spinner spinnerColor6;

    private SeekBar sbSec;
    private SeekBar sbMin;
    private SeekBar sbHour;

    private Switch backgroundSwitch;
    private Spinner spinnerDigitsType;

    private Switch digitSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        spinnerColor = findViewById(R.id.spinnerDigitsColor);
        spinnerColor2 = findViewById(R.id.spinnerDigitsColor2);
        spinnerColor3 = findViewById(R.id.spinnerDigitsColor3);
        spinnerColor4 = findViewById(R.id.spinnerDigitsColor4);
        spinnerColor5 = findViewById(R.id.spinnerDigitsColor5);
        spinnerColor6 = findViewById(R.id.spinnerDigitsColor6);

        ColorSpinnerAdapter colorSpinnerAdapter = new ColorSpinnerAdapter(getApplicationContext(), SettingManager.getInstance().getColorList());
        spinnerColor.setAdapter(colorSpinnerAdapter);
        spinnerColor2.setAdapter(colorSpinnerAdapter);
        spinnerColor3.setAdapter(colorSpinnerAdapter);
        spinnerColor4.setAdapter(colorSpinnerAdapter);
        spinnerColor5.setAdapter(colorSpinnerAdapter);
        spinnerColor6.setAdapter(colorSpinnerAdapter);

        sbSec = findViewById(R.id.sbSec);
        final TextView sbSecVal = findViewById(R.id.handSecVal);
        setupListenerSeekBar(sbSec, sbSecVal);

        sbMin = findViewById(R.id.sbMin);
        TextView sbMinVal = findViewById(R.id.handMinVal);
        setupListenerSeekBar(sbMin, sbMinVal);

        sbHour = findViewById(R.id.sbHour);
        TextView sbHourVal = findViewById(R.id.handHourVal);
        setupListenerSeekBar(sbHour, sbHourVal);

        backgroundSwitch = findViewById(R.id.backgroundSwitch);
        spinnerDigitsType = findViewById(R.id.spinnerDigitsType);
        digitSwitch = findViewById(R.id.digitSwitch);

        setupLayout();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.preferences_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reset:
                SettingManager.getInstance().resetPreferences(this);
                setupLayout();
                Toast.makeText(this, "Preferences reseted", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_save:
                save();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupListenerSeekBar(SeekBar sb, final TextView tv) {
        tv.setText("" + sb.getProgress());
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tv.setText("" + (i+1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void save() {
        Map<String, Object> preferences = new HashMap<>();

        preferences.put(getString(R.string.preferences_key_theme), backgroundSwitch.isChecked());
        preferences.put(getString(R.string.preferences_key_digit_type), spinnerDigitsType.getSelectedItemPosition());

        preferences.put(getString(R.string.preferences_key_digit_color), spinnerColor.getSelectedItemPosition());
        preferences.put(getString(R.string.preferences_key_digit_display), digitSwitch.isChecked());

        preferences.put(getString(R.string.preferences_key_hand_sec_thick), sbSec.getProgress());
        preferences.put(getString(R.string.preferences_key_hand_sec_color), spinnerColor2.getSelectedItemPosition());

        preferences.put(getString(R.string.preferences_key_hand_min_thick), sbMin.getProgress());
        preferences.put(getString(R.string.preferences_key_hand_min_color),spinnerColor3.getSelectedItemPosition());

        preferences.put(getString(R.string.preferences_key_hand_hour_thick), sbHour.getProgress());
        preferences.put(getString(R.string.preferences_key_hand_hour_color), spinnerColor4.getSelectedItemPosition());

        preferences.put(getString(R.string.preferences_key_mark_hour_color), spinnerColor5.getSelectedItemPosition());
        preferences.put(getString(R.string.preferences_key_mark_sec_color), spinnerColor6.getSelectedItemPosition());

        SettingManager.getInstance().savePreferences(this, preferences);
        Toast.makeText(this, "Preferences saved", Toast.LENGTH_SHORT).show();
    }

    private void setupLayout() {
        Map<String, Object> preferences = SettingManager.getInstance().getPreferencesMap();

        backgroundSwitch.setChecked((boolean)preferences.get(getString(R.string.preferences_key_theme)));
        spinnerDigitsType.setSelection((int)preferences.get(getString(R.string.preferences_key_digit_type)));

        spinnerColor.setSelection((int)preferences.get(getString(R.string.preferences_key_digit_color)));
        digitSwitch.setChecked((boolean)preferences.get(getString(R.string.preferences_key_digit_display)));

        sbSec.setProgress((int)preferences.get(getString(R.string.preferences_key_hand_sec_thick)));
        spinnerColor2.setSelection((int)preferences.get(getString(R.string.preferences_key_hand_sec_color)));

        sbMin.setProgress((int)preferences.get(getString(R.string.preferences_key_hand_min_thick)));
        spinnerColor3.setSelection((int)preferences.get(getString(R.string.preferences_key_hand_min_color)));

        sbHour.setProgress((int)preferences.get(getString(R.string.preferences_key_hand_hour_thick)));
        spinnerColor4.setSelection((int)preferences.get(getString(R.string.preferences_key_hand_hour_color)));

        spinnerColor5.setSelection((int)preferences.get(getString(R.string.preferences_key_mark_hour_color)));
        spinnerColor6.setSelection((int)preferences.get(getString(R.string.preferences_key_mark_sec_color)));
    }

}
