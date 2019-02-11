package tortel.fr.myclock.drawing;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class RegPoly {
    private float x0, y0, r;
    private int n;
    private float x[], y[];

    private Canvas canvas;
    private Paint paint;

    public RegPoly(int n, float r, float x0, float y0, Canvas canvas, Paint paint) {
        this.n = n;
        this.r = r;
        this.x0 = x0;
        this.y0 = y0;
        this.canvas = canvas;
        this.paint = paint;

        x = new float[n];
        y = new float[n];

        for (int i = 0; i < n; i++) {
            x[i] = (float) (x0 + r * Math.cos((2*Math.PI * i)/n));
            y[i] = (float) (y0 + r * Math.sin((2*Math.PI * i)/n));
        }
    }

    public float getX(int i) {
        return x[i%n];
    }

    public float getY(int i) {
        return y[i%n];
    }

    public void drawRegPoly() {

        for (int i = 0; i < n; i++) {
            canvas.drawLine(x[i], y[i], x[(i+1)%n], y[(i+1)%n], paint);
        }
    }

    public void drawPoints() {
        for (int i = 0; i < n; i++) {
            canvas.drawCircle(x[i], y[i], 4, paint);
        }
    }

    public void drawRadius(int i) {
        canvas.drawLine(x0, y0, x[i%n], y[i%n], paint);
    }

    public void drawText(int i, String text, float textSize) {
        paint.setTextSize(textSize);
        float halfTextHeight = (paint.descent() + paint.ascent()) / 2;
        canvas.drawText(text, x[i%n], y[i%n] - halfTextHeight, paint);
    }
}
