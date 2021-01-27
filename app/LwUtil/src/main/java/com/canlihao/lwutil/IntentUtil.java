package com.canlihao.lwutil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

/*******
 *
 *Created by panda李
 *
 *创建时间 2020/3/30
 *
 *类描述：
 *
 ********/
////使用方法
// IntentUtil.get().goActivity(this, MainActivity.class);
public class IntentUtil {
    public static final String OPEN_ACTIVITY_KEY = "open_activity";//intent跳转传递传输key
    private static IntentUtil manager;
    private static Intent intent;

    private IntentUtil() {

    }

    public static IntentUtil get() {
        if (manager == null) {
            synchronized (IntentUtil.class) {
                if (manager == null) {
                    manager = new IntentUtil();
                }
            }
        }
        intent = new Intent();
        return manager;
    }

    /**
     * 获取上一个界面传递过来的参数
     *
     * @param activity this
     * @param <T>      泛型
     * @return
     */
    public <T> T getParcelableExtra(Activity activity) {
        Parcelable parcelable = activity.getIntent().getParcelableExtra(OPEN_ACTIVITY_KEY);
        activity = null;
        return (T) parcelable;
    }
    /**
     * 获取上一个界面传递过来的参数
     *
     * @param activity this
     * @param <T>      泛型
     * @return
     */
    public <T> T getParcelableExtraFromKey(Activity activity, String key) {
        Parcelable parcelable = activity.getIntent().getParcelableExtra(key);
        activity = null;
        return (T) parcelable;
    }

    /**
     * 启动一个Activity
     *
     * @param _this
     * @param _class
     */
    public void goActivity(Context _this, Class<? extends Activity> _class) {
        intent.setClass(_this, _class);
        _this.startActivity(intent);
        _this = null;
    }

    /**
     * 启动activity后kill前一个
     *
     * @param _this
     * @param _class
     */
    public void goActivityKill(Context _this, Class<? extends Activity> _class) {
        intent.setClass(_this, _class);
        _this.startActivity(intent);
        ((Activity) _this).finish();
        _this = null;
    }

    /**
     * 回调跳转
     *
     * @param _this
     * @param _class
     * @param requestCode
     */
    public void goActivityResult(Activity _this, Class<? extends Activity> _class, int requestCode) {
        intent.setClass(_this, _class);
        _this.startActivityForResult(intent, requestCode);
        _this = null;
    }

    /**
     * 回调跳转并finish当前页面
     *
     * @param _this
     * @param _class
     * @param requestCode
     */
    public void goActivityResultKill(Activity _this, Class<? extends Activity> _class, int requestCode) {
        intent.setClass(_this, _class);
        _this.startActivityForResult(intent, requestCode);
        ((Activity) _this).finish();
        _this = null;
    }

    /**
     * 传递一个序列化实体类
     *
     * @param _this
     * @param _class
     * @param parcelable
     */
    public void goActivity(Context _this, Class<? extends Activity> _class, Parcelable parcelable) {
        intent.setClass(_this, _class);
        putParcelable(parcelable);
        _this.startActivity(intent);
        _this = null;
    }

    /**
     * 启动一个Activity
     *
     * @param _this
     * @param _class
     * @param flags
     * @param parcelable 传递的实体类
     */
    public void goActivity(Context _this, Class<? extends Activity> _class, int flags, Parcelable parcelable) {
        intent.setClass(_this, _class);
        setFlags(flags);
        putParcelable(parcelable);
        _this.startActivity(intent);
        _this = null;
    }

    public void goActivityKill(Context _this, Class<? extends Activity> _class, Parcelable parcelable) {
        intent.setClass(_this, _class);
        putParcelable(parcelable);
        _this.startActivity(intent);
        ((Activity) _this).finish();
        _this = null;
    }

    public void goActivityKill(Context _this, Class<? extends Activity> _class, int flags, Parcelable parcelable) {
        intent.setClass(_this, _class);
        setFlags(flags);
        putParcelable(parcelable);
        _this.startActivity(intent);
        ((Activity) _this).finish();
        _this = null;
    }

    public void goActivityResult(Activity _this, Class<? extends Activity> _class, Parcelable parcelable, int requestCode) {
        intent.setClass(_this, _class);
        putParcelable(parcelable);
        _this.startActivityForResult(intent, requestCode);
        _this = null;
    }

    public void goActivityResult(Activity _this, Class<? extends Activity> _class, int flags, Parcelable parcelable, int requestCode) {
        intent.setClass(_this, _class);
        putParcelable(parcelable);
        setFlags(flags);
        _this.startActivityForResult(intent, requestCode);
        _this = null;
    }

    public void goActivityResultKill(Activity _this, Class<? extends Activity> _class, Parcelable parcelable, int requestCode) {
        intent.setClass(_this, _class);
        putParcelable(parcelable);
        _this.startActivityForResult(intent, requestCode);
        _this.finish();
        _this = null;
    }

    public void goActivityResultKill(Activity _this, Class<? extends Activity> _class, int flags, Parcelable parcelable, int requestCode) {
        intent.setClass(_this, _class);
        putParcelable(parcelable);
        setFlags(flags);
        _this.startActivityForResult(intent, requestCode);
        _this.finish();
        _this = null;
    }

    private void setFlags(int flags) {
        if (flags < 0) return;
        intent.setFlags(flags);
    }

    private void putParcelable(Parcelable parcelable) {
        if (parcelable == null) return;
        intent.putExtra(OPEN_ACTIVITY_KEY, parcelable);
    }
}
