package com.canlihao.lwutil;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.widget.Toast;

import java.util.List;


/*******
 *
 *Created by 李魏
 *
 *创建时间 2019/6/24
 *
 *类描述：
 *
 ********/
public class ToastUtil {
    public static void showToast(Context context, String mgs) {
        if(Tools.isEmpty(mgs)) return;
        Toast mToast = Toast.makeText(context, null, Toast.LENGTH_SHORT);
        mToast.setText(mgs);
        mToast.show();
    }
    /**
     * 判断某个界面是否在前台
     *
     * @param activity 要判断的Activity
     * @return 是否在前台显示
     */
    public static boolean isForeground(Activity activity) {
        return isForeground(activity, activity.getClass().getName());
    }
    /**
     * 判断某个界面是否在前台
     *
     * @param context   Context
     * @param className 界面的类名
     * @return 是否在前台显示
     */
    public static boolean isForeground(Context context, String className) {
        if (context == null || Tools.isEmpty(className))
            return false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName()))
                return true;
        }
        return false;
    }
}
