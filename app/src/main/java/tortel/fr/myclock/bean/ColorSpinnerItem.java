package tortel.fr.myclock.bean;

public class ColorSpinnerItem {
    private String colorName;
    private int color;

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public ColorSpinnerItem(String colorName, int color) {
        this.colorName = colorName;
        this.color = color;
    }
}
