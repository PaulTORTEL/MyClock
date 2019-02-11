package tortel.fr.myclock.drawing;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Calendar;
import java.util.Map;

import tortel.fr.myclock.MainActivity;
import tortel.fr.myclock.R;
import tortel.fr.myclock.manager.SettingManager;

public class ClockSurfaceView extends SurfaceView implements Runnable {

    private Thread thread;
    private SurfaceHolder surfaceHolder;
    private boolean running = false;
    private float length;
    private final int SHIFT = 45;
    private MainActivity activity;
    private String numeric[] = new String[]{"12", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"};
    private String roman[] = new String[]{"XII", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI"};
    private final String COLOR = "color_";

    public ClockSurfaceView(Context context, float length, Activity activity) {
        super(context);
        this.length = length;
        surfaceHolder = this.getHolder();
        this.activity = (MainActivity) activity;
    }

    public void onClockResume() {
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public void onClockPause() {
        boolean retry = true;
        running = false;

        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                Log.e("error", e.getMessage());
            }
        }
    }

    public void run() {

        Map<String, Object> preferences = SettingManager.getInstance().getPreferencesMap();
        boolean darkTheme = (boolean) preferences.get(activity.getString(R.string.preferences_key_theme));
        int digitType = (int) preferences.get(activity.getString(R.string.preferences_key_digit_type));
        int digitColor = (int) preferences.get(activity.getString(R.string.preferences_key_digit_color));
        boolean digitDisplay = (boolean) preferences.get(activity.getString(R.string.preferences_key_digit_display));
        int handSecThick = (int) preferences.get(activity.getString(R.string.preferences_key_hand_sec_thick));
        int handSecColor = (int) preferences.get(activity.getString(R.string.preferences_key_hand_sec_color));
        int handMinThick = (int) preferences.get(activity.getString(R.string.preferences_key_hand_min_thick));
        int handMinColor = (int) preferences.get(activity.getString(R.string.preferences_key_hand_min_color));
        int handHourThick = (int) preferences.get(activity.getString(R.string.preferences_key_hand_hour_thick));
        int handHourColor = (int) preferences.get(activity.getString(R.string.preferences_key_hand_hour_color));
        int markHourColor = (int) preferences.get(activity.getString(R.string.preferences_key_mark_hour_color));
        int markSecColor = (int) preferences.get(activity.getString(R.string.preferences_key_mark_sec_color));

        Resources res = activity.getResources();

        while (running) {
            long startTime = System.currentTimeMillis();

            if (surfaceHolder.getSurface().isValid()) {
                Calendar now = Calendar.getInstance();
                int hour = now.get(Calendar.HOUR);
                int minute = now.get(Calendar.MINUTE);
                int second = now.get(Calendar.SECOND);

                Canvas canvas = surfaceHolder.lockCanvas();
                if (darkTheme) {
                    canvas.drawColor(activity.getResources().getColor(R.color.color_1));
                } else {
                    canvas.drawColor(activity.getResources().getColor(R.color.color_8));
                }

                Paint paint = new Paint();
                if (darkTheme) {
                    paint.setColor(activity.getResources().getColor(R.color.color_8));
                } else {
                    paint.setColor(activity.getResources().getColor(R.color.color_7));
                }
                paint.setTextAlign(Paint.Align.CENTER);
                RegPoly title = new RegPoly(60, length + 100, getWidth()/2, getHeight()/2, canvas, paint);
                title.drawText(SHIFT, "Clock", 60);

                paint.setColor(res.getColor(res.getIdentifier(COLOR + (markSecColor+1), "color", activity.getPackageName())));
                RegPoly secondMarks = new RegPoly(60, length, getWidth()/2, getHeight()/2, canvas, paint);
                secondMarks.drawPoints();

                paint.setColor(res.getColor(res.getIdentifier(COLOR + (markHourColor+1), "color", activity.getPackageName())));
                RegPoly hourMarks = new RegPoly(12, length - 20, getWidth()/2, getHeight()/2, canvas, paint);
                hourMarks.drawPoints();

                paint.setStrokeWidth(handSecThick+1);
                paint.setColor(res.getColor(res.getIdentifier(COLOR + (handSecColor+1), "color", activity.getPackageName())));
                RegPoly secondHand = new RegPoly(60, length-30, getWidth()/2, getHeight()/2, canvas, paint);
                secondHand.drawRadius(second + SHIFT);

                paint.setStrokeWidth(handMinThick+1);
                paint.setColor(res.getColor(res.getIdentifier(COLOR + (handMinColor+1), "color", activity.getPackageName())));
                RegPoly minuteHand = new RegPoly(60, length-50, getWidth()/2, getHeight()/2, canvas, paint);
                minuteHand.drawRadius(minute + SHIFT);

                paint.setStrokeWidth(handHourThick+1);
                paint.setColor(res.getColor(res.getIdentifier(COLOR + (handHourColor+1), "color", activity.getPackageName())));
                RegPoly hourHand = new RegPoly(60, length-120, getWidth()/2, getHeight()/2, canvas, paint);
                hourHand.drawRadius(hour * 5 + minute / 12 + SHIFT);

                writeDigits(canvas, paint, digitType, digitDisplay, digitColor);

                surfaceHolder.unlockCanvasAndPost(canvas);
                try {
                    long endTime = System.currentTimeMillis();
                    long totalTime = endTime - startTime;
                    Thread.sleep(1000 - totalTime);
                } catch (InterruptedException e) {
                    Log.e("error", e.getMessage());
                }
            }
        }
    }

    private void writeDigits(Canvas canvas, Paint paint, int digitType, boolean digitDisplay, int digitColor) {
        paint.setColor(activity.getResources().getColor(activity.getResources().getIdentifier(COLOR + (digitColor+1), "color", activity.getPackageName())));
        final float lengthDigits = length - 60;

        int counter = 0;
        for (int i = 0; i < 60; i += 5) {
            if (!digitDisplay && i % 3 != 0) {
                counter++;
                continue;
            }

            RegPoly digit = new RegPoly(60, lengthDigits, getWidth()/2, getHeight()/2, canvas, paint);
            digit.drawText(i + SHIFT, digitType == 0 ? numeric[counter]:roman[counter], 35);
            counter++;
        }
    }
}
