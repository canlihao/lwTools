package com.canlihao.lwutil.sticky;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.StyleableRes;

import com.canlihao.lwutil.sticky.lis.IResourceProvider;


/**
 * Created by Vbe on 2020/12/17.
 */
public class ResourceProvider implements IResourceProvider {

    private final TypedArray mTypeArray;

    public ResourceProvider(Context context, AttributeSet attrs, @StyleableRes int[] styleRes) {
        mTypeArray = context.obtainStyledAttributes(attrs, styleRes);
    }

    @Override
    public int getResourceId(@StyleableRes int styleResId) {
        return mTypeArray.getResourceId(styleResId, 0);
    }

    @Override
    public void recycle() {
        mTypeArray.recycle();
    }
}
