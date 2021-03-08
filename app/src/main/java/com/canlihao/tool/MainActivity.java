package com.canlihao.tool;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.canlihao.lwutil.GalleryUtil;
import com.canlihao.lwutil.MyLog;
import com.canlihao.lwutil.ToastUtil;
import com.canlihao.lwutil.Tools;
import com.canlihao.lwutil.matisse.entity.CaptureStrategy;
import com.canlihao.lwutil.media.MimeType;
import com.google.gson.Gson;

import java.io.File;
import java.util.List;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * @Author : Panda李
 * @Time : On 2021/1/27 10:26
 * @Description : MainActivity
 */
public class MainActivity extends AppCompatActivity {
    AlertDialog dialog;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init();
            }
        });
    }

    private void init() {
        if (Tools.hasAllPermissions(this, Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE
                ,Manifest.permission.READ_EXTERNAL_STORAGE
                ,Manifest.permission.ACCESS_FINE_LOCATION
                ,Manifest.permission.ACCESS_COARSE_LOCATION)){
            ToastUtil.showToast(this,"相机权限打开了");
            GalleryUtil.from(MainActivity.this)
                    .choose(MimeType.ofImage())
                    .maxSelectable(1)
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                    .capture(true)
                    .captureStrategy(new CaptureStrategy(true, getApplication().getPackageName() + ".fileprovider"))
                    .theme(R.style.Theme_Tool)
                    .forResult(147);
        }else{
            MyLog.ii("请求相机权限");
            Tools.requestPermission(this,1024,Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION
                    ,Manifest.permission.ACCESS_COARSE_LOCATION);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MyLog.ii("--"+requestCode+"--"+new Gson().toJson(permissions) +"--"+new Gson().toJson(grantResults) +"--"+grantResults[0]);
        if(grantResults.length > 0){
            ToastUtil.showToast(this,"相机权限打开了");
            GalleryUtil.from(MainActivity.this)
                    .choose(MimeType.ofImage())
                    .maxSelectable(1)
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                    .capture(true)
                    .captureStrategy(new CaptureStrategy(true, getApplication().getPackageName() + ".fileprovider"))
                    .theme(R.style.Theme_Tool)
                    .forResult(147);
        }else{
            showWaringDialog();
        }
    }

    private void showWaringDialog() {
        dialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle("警告！")
                .setMessage("请前往设置里的权限管理中打开相关权限，否则功能无法正常运行！")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }

                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }

                }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            List<String> mSelected = GalleryUtil.obtainPathResult(data);
            if (mSelected.size() > 0) {
                String path = mSelected.get(0);
                MyLog.ii("路径="+path);
                Luban.with(this).load(path)
                        .ignoreBy(350)
                        .setTargetDir(getCacheFilePath(this))
                        .setCompressListener(new OnCompressListener() {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onSuccess(File file) {
//                                uploadImage(file, requestCode);
                                MyLog.i("uploadImage", file.getAbsolutePath());
                            }

                            @Override
                            public void onError(Throwable e) {
                                MyLog.ii("压缩图片失败" + e.toString());
                            }
                        }).launch();
                Log.i("dataUri", path);
            }
        }
    }
    public static String getCacheFilePath(Context context) {
        return context.getCacheDir().getAbsolutePath();
        /*String path = context.getCacheDir() + File.separator + "tmpImg";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;*/
    }
}
