package tortel.fr.myclock.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import tortel.fr.myclock.R;
import tortel.fr.myclock.bean.ColorSpinnerItem;

public class ColorSpinnerAdapter extends ArrayAdapter<ColorSpinnerItem> {

    public ColorSpinnerAdapter(@NonNull Context context, List<ColorSpinnerItem> colorSpinnerList) {
        super(context, 0, colorSpinnerList);
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
            converView = LayoutInflater.from(getContext()).inflate(R.layout.color_spinner, parent, false);
        }

        View colorView = converView.findViewById(R.id.color);
        TextView colorNameTv = converView.findViewById(R.id.colorName);

        ColorSpinnerItem currItem = getItem(position);

        if (currItem != null) {
            int color = currItem.getColor();
            String colorName = currItem.getColorName();

            colorView.setBackgroundColor(color);
            colorNameTv.setText(colorName);
        }

        return converView;
    }
}
