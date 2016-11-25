package example.com.projectone.util;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import java.util.Random;

/**
 * Created by Abhinav Ravi on 05/11/16.
 */
public class ColorUtil {

    private ColorDrawable[] vibrantLightColorList =
            {
                    new ColorDrawable(Color.parseColor("#607D8B")), new ColorDrawable(Color.parseColor("#FF6E40")),
                    new ColorDrawable(Color.parseColor("#FFEA00")), new ColorDrawable(Color.parseColor("#FF9100")),
                    new ColorDrawable(Color.parseColor("#FFF176")), new ColorDrawable(Color.parseColor("#C6FF00")),
                    new ColorDrawable(Color.parseColor("#00E676")), new ColorDrawable(Color.parseColor("#CDDC39")),
                    new ColorDrawable(Color.parseColor("#00B0FF")), new ColorDrawable(Color.parseColor("#1DE9B6")),
                    new ColorDrawable(Color.parseColor("#0288D1")), new ColorDrawable(Color.parseColor("#00897B")),
                    new ColorDrawable(Color.parseColor("#7C4DFF")), new ColorDrawable(Color.parseColor("#FF1744")),
                    new ColorDrawable(Color.parseColor("#F44336")), new ColorDrawable(Color.parseColor("#D500F9"))
            };

    public ColorDrawable getRandomDrawbleColor() {
        int idx = new Random().nextInt(vibrantLightColorList.length);
        return vibrantLightColorList[idx];
    }
}
