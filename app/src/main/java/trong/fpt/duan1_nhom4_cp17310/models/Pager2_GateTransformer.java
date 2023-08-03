package trong.fpt.duan1_nhom4_cp17310.models;

import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.viewpager2.widget.ViewPager2;

@RequiresApi(21)
public class Pager2_GateTransformer implements ViewPager2.PageTransformer {
    @Override
    public void transformPage(@NonNull View page, float position) {
        page.setTranslationX(-position*page.getWidth());
        if (position<-1){    // [-Infinity,-1)
            // This page is way off-screen to the left
            page.setAlpha(0);
        }
        else if (position<=0){    // [-1,0]
            page.setAlpha(1);
            page.setPivotX(0);
            page.setPivotY((float) (0.6*page.getHeight()));
            page.setRotationY(10*Math.abs(position));
        }
        else if (position <=4){    // (0,1]
            page.setAlpha(1);
            page.setPivotX(page.getWidth());
            page.setPivotY((float) (0.6*page.getHeight()));
            page.setRotationY(-10*Math.abs(position));
        }
        else {    // (1,+Infinity]
            // This page is way off-screen to the right.
            page.setAlpha(0);
        }

    }
}