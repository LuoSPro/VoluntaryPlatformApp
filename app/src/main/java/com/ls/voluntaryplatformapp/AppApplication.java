package com.ls.voluntaryplatformapp;

import android.app.Application;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ls.libnetwork.ApiService;
import com.ls.libnetwork.Convert;

import java.lang.reflect.Type;

public class AppApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ApiService.isRestfulStyle = true;
        //初始化网络请求module，否则后面会报空指针
        ApiService.init("http://192.168.31.110:80", new Convert() {
            @Override
            public Object convert(String response, Type type) {
                JSONObject jsonObject = JSON.parseObject(response);
                Object data = jsonObject.get("data");
                if (jsonObject != null){
                    //获取data里面的data字段，
                    return JSON.parseObject(data.toString(),type);
                }
                return null;
            }

            @Override
            public Object convert(String response, Class claz) {
                JSONObject jsonObject = JSON.parseObject(response);
                JSONObject data = jsonObject.getJSONObject("data");
                if (data != null){
                    Object innerData = data.get("data");
                    return JSON.parseObject(innerData.toString(),claz);
                }
                return null;
            }
        });
    }
}
