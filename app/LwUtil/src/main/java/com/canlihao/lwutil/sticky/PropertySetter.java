package com.canlihao.lwutil.sticky;

import android.view.View;

import androidx.core.view.ViewCompat;

import com.canlihao.lwutil.Tools;


/**
 * Created by Vbe on 2020/12/17.
 */
public class PropertySetter {

    public static void setTranslationZ(View view, float translationZ) {
        if (Tools.isAndroidL()) {
            ViewCompat.setTranslationZ(view, translationZ);
        } else if (translationZ != 0) {
            view.bringToFront();
            if (view.getParent() != null) {
                ((View) view.getParent()).invalidate();
            }
        }
    }
}
