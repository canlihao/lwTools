package com.canlihao.lwutil;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/*******
 *
 *Created by 李魏
 *
 *创建时间 2020/1/17
 *
 *类描述：
 *
 ********/
public class IMEUtil { 
    /**
     * 关闭输入法
     */
    public static void closeIME(Context context) {
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        try {
            View focusView = ((Activity) context).getCurrentFocus();
            if (manager != null && manager.isActive() && null != focusView) {
                manager.hideSoftInputFromWindow(focusView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示输入法
     */
    public static void showIME(Context context) {
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    /**
     * 隐藏软键盘
     */
    public static void hideSoftKeyboard(View view) {
        ((InputMethodManager) view.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                view.getWindowToken(), 0);
    }

    /**
     * 显示软键盘
     */
    public static void showSoftKeyboard(View view) {
        ((InputMethodManager) view.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE)).showSoftInput(
                view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 获取键盘状态
     */
    public static boolean isActiveState(Context context) {
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        return manager.isActive();
    }
}
