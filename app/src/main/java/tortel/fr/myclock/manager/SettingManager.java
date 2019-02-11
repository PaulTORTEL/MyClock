package tortel.fr.myclock.manager;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tortel.fr.myclock.R;
import tortel.fr.myclock.bean.ColorSpinnerItem;

public class SettingManager {
    private static final SettingManager instance = new SettingManager();
    private boolean init = false;
    private final String PREFERENCES_FILE_NAME = "PREF_MY_CLOCK";

    private Map<String, Object> preferencesMap = new HashMap<>();
    private Map<String, Object> originalPreferencesMap = new HashMap<>();
    private List<ColorSpinnerItem> colorList = new ArrayList<>();

    public static SettingManager getInstance() {
        return instance;
    }

    private SettingManager() {
    }

    public boolean isInit() {
        return init;
    }

    public void init(Activity activity) {
        if (init) {
            return;
        }

        init = true;
        SharedPreferences sharedPref = activity.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        final boolean theme = sharedPref.getBoolean(activity.getString(R.string.preferences_key_theme), true);
        final int digitType = sharedPref.getInt(activity.getString(R.string.preferences_key_digit_type), 1);
        final int digitColor = sharedPref.getInt(activity.getString(R.string.preferences_key_digit_color), 7);
        final boolean digitDisplay = sharedPref.getBoolean(activity.getString(R.string.preferences_key_digit_display), false);
        final int handSecThick = sharedPref.getInt(activity.getString(R.string.preferences_key_hand_sec_thick), 4);
        final int handSecColor = sharedPref.getInt(activity.getString(R.string.preferences_key_hand_sec_color), 5);
        final int handMinThick = sharedPref.getInt(activity.getString(R.string.preferences_key_hand_min_thick), 6);
        final int handMinColor = sharedPref.getInt(activity.getString(R.string.preferences_key_hand_min_color), 1);
        final int handHourThick = sharedPref.getInt(activity.getString(R.string.preferences_key_hand_hour_thick), 9);
        final int handHourColor = sharedPref.getInt(activity.getString(R.string.preferences_key_hand_hour_color), 1);
        final int markHourColor = sharedPref.getInt(activity.getString(R.string.preferences_key_mark_hour_color), 4);
        final int markSecColor = sharedPref.getInt(activity.getString(R.string.preferences_key_mark_sec_color), 3);

        preferencesMap.clear();
        originalPreferencesMap.clear();
        colorList.clear();

        preferencesMap.put(activity.getString(R.string.preferences_key_theme), theme);
        preferencesMap.put(activity.getString(R.string.preferences_key_digit_type), digitType);
        preferencesMap.put(activity.getString(R.string.preferences_key_digit_color), digitColor);
        preferencesMap.put(activity.getString(R.string.preferences_key_digit_display), digitDisplay);
        preferencesMap.put(activity.getString(R.string.preferences_key_hand_sec_thick), handSecThick);
        preferencesMap.put(activity.getString(R.string.preferences_key_hand_sec_color), handSecColor);
        preferencesMap.put(activity.getString(R.string.preferences_key_hand_min_thick), handMinThick);
        preferencesMap.put(activity.getString(R.string.preferences_key_hand_min_color), handMinColor);
        preferencesMap.put(activity.getString(R.string.preferences_key_hand_hour_thick), handHourThick);
        preferencesMap.put(activity.getString(R.string.preferences_key_hand_hour_color), handHourColor);
        preferencesMap.put(activity.getString(R.string.preferences_key_mark_hour_color), markHourColor);
        preferencesMap.put(activity.getString(R.string.preferences_key_mark_sec_color), markSecColor);

        originalPreferencesMap.put(activity.getString(R.string.preferences_key_theme), true);
        originalPreferencesMap.put(activity.getString(R.string.preferences_key_digit_type), 1);
        originalPreferencesMap.put(activity.getString(R.string.preferences_key_digit_color), 7);
        originalPreferencesMap.put(activity.getString(R.string.preferences_key_digit_display), false);
        originalPreferencesMap.put(activity.getString(R.string.preferences_key_hand_sec_thick), 4);
        originalPreferencesMap.put(activity.getString(R.string.preferences_key_hand_sec_color), 5);
        originalPreferencesMap.put(activity.getString(R.string.preferences_key_hand_min_thick), 6);
        originalPreferencesMap.put(activity.getString(R.string.preferences_key_hand_min_color), 1);
        originalPreferencesMap.put(activity.getString(R.string.preferences_key_hand_hour_thick), 9);
        originalPreferencesMap.put(activity.getString(R.string.preferences_key_hand_hour_color), 1);
        originalPreferencesMap.put(activity.getString(R.string.preferences_key_mark_hour_color), 4);
        originalPreferencesMap.put(activity.getString(R.string.preferences_key_mark_sec_color), 3);

        setupColorList(activity);
    }

    private void setupColorList(Activity activity) {
        colorList.add(new ColorSpinnerItem("Black", activity.getResources().getColor(R.color.color_1)));
        colorList.add(new ColorSpinnerItem("Orange", activity.getResources().getColor(R.color.color_2)));
        colorList.add(new ColorSpinnerItem("Red", activity.getResources().getColor(R.color.color_3)));
        colorList.add(new ColorSpinnerItem("Green", activity.getResources().getColor(R.color.color_4)));
        colorList.add(new ColorSpinnerItem("Turquoise", activity.getResources().getColor(R.color.color_5)));
        colorList.add(new ColorSpinnerItem("Yellow", activity.getResources().getColor(R.color.color_6)));
        colorList.add(new ColorSpinnerItem("Blue", activity.getResources().getColor(R.color.color_7)));
        colorList.add(new ColorSpinnerItem("White", activity.getResources().getColor(R.color.color_8)));
    }

    public void savePreferences(Activity activity, Map<String, Object> newPreferencesMap) {
        preferencesMap.clear();
        preferencesMap.putAll(newPreferencesMap);

        SharedPreferences sharedPref = activity.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        String theme = activity.getString(R.string.preferences_key_theme);
        editor.putBoolean(theme, (boolean)preferencesMap.get(theme));

        String digitType = activity.getString(R.string.preferences_key_digit_type);
        editor.putInt(digitType, (int)preferencesMap.get(digitType));

        String digitColor = activity.getString(R.string.preferences_key_digit_color);
        editor.putInt(digitColor, (int)preferencesMap.get(digitColor));

        String digitDisplay = activity.getString(R.string.preferences_key_digit_display);
        editor.putBoolean(digitDisplay, (boolean)preferencesMap.get(digitDisplay));

        String handSecThick = activity.getString(R.string.preferences_key_hand_sec_thick);
        editor.putInt(handSecThick, (int)preferencesMap.get(handSecThick));

        String handSecColor = activity.getString(R.string.preferences_key_hand_sec_color);
        editor.putInt(handSecColor, (int)preferencesMap.get(handSecColor));

        String handMinThick = activity.getString(R.string.preferences_key_hand_min_thick);
        editor.putInt(handMinThick, (int)preferencesMap.get(handMinThick));

        String handMinColor = activity.getString(R.string.preferences_key_hand_min_color);
        editor.putInt(handMinColor, (int)preferencesMap.get(handMinColor));

        String handHourThick = activity.getString(R.string.preferences_key_hand_hour_thick);
        editor.putInt(handHourThick, (int)preferencesMap.get(handHourThick));

        String handHourColor = activity.getString(R.string.preferences_key_hand_hour_color);
        editor.putInt(handHourColor, (int)preferencesMap.get(handHourColor));

        String markHourColor = activity.getString(R.string.preferences_key_mark_hour_color);
        editor.putInt(markHourColor, (int)preferencesMap.get(markHourColor));

        String markSecColor = activity.getString(R.string.preferences_key_mark_sec_color);
        editor.putInt(markSecColor, (int)preferencesMap.get(markSecColor));

        editor.apply();
    }

    public void resetPreferences(Activity activity) {
       savePreferences(activity, originalPreferencesMap);
    }

    public Map<String, Object> getPreferencesMap() {
        return preferencesMap;
    }

    public List<ColorSpinnerItem> getColorList() {
        return colorList;
    }
}
