package com.canlihao.tool;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.canlihao.lwutil.MyLog;
import com.canlihao.lwutil.ToastUtil;
import com.canlihao.lwutil.Tools;
import com.google.gson.Gson;

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
        if (Tools.hasAllPermissions(this, Manifest.permission.CAMERA)){
            ToastUtil.showToast(this,"相机权限打开了");
        }else{
            MyLog.ii("请求相机权限");
            Tools.requestPermission(this,1024,Manifest.permission.CAMERA);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MyLog.ii("--"+requestCode+"--"+new Gson().toJson(permissions) +"--"+new Gson().toJson(grantResults) +"--"+grantResults[0]);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            ToastUtil.showToast(this,"相机权限打开了");
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
}
