package com.ls.voluntaryplatformapp;

import android.app.Application;

import com.ls.libnetwork.ApiService;

public class AppApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化网络请求module，否则后面会报空指针
        ApiService.init("http://192.168.10.108:8888/serverdemo",null);
    }
}
