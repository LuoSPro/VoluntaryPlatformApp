package com.ls.voluntaryplatformapp.utils;

import android.content.res.AssetManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ls.libcommon.global.AppGlobals;
import com.ls.voluntaryplatformapp.model.ActionTab;
import com.ls.voluntaryplatformapp.model.BottomBar;
import com.ls.voluntaryplatformapp.model.Destination;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class AppConfig {

    private static HashMap<String, Destination> sDestConfig;

    private static BottomBar sBottomBar;

    private static ActionTab sActionTab;

    public static HashMap<String, Destination> getDestConfig() {
        if (sDestConfig == null){
            //从destination.json文件中解析出sDestConfig对象
            String content = parseFile("destination.json");
            sDestConfig = JSON.parseObject(content,new TypeReference<HashMap<String,Destination>>(){}.getType());
        }
        return sDestConfig;
    }

    public static BottomBar getBottomBar(){
        if (sBottomBar == null){
            String content = parseFile("main_tabs_config.json");
            sBottomBar = JSON.parseObject(content,BottomBar.class);
        }
        return sBottomBar;
    }

    public static ActionTab getActionTab(){
        if (sActionTab == null){
            String content = parseFile("action_tabs_config.json");
            sActionTab = JSON.parseObject(content,ActionTab.class);
            Collections.sort(sActionTab.getTabs(), new Comparator<ActionTab.Tabs>() {
                @Override
                public int compare(ActionTab.Tabs o1, ActionTab.Tabs o2) {
                    return o1.getIndex() < o2.getIndex() ? -1 : 1;
                }
            });
        }
        return sActionTab;
    }

    public static String parseFile(String fileName) {
        AssetManager assets = AppGlobals.getApplication().getResources().getAssets();
        InputStream stream = null;
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            stream = assets.open(fileName);
            reader = new BufferedReader(new InputStreamReader(stream));
            String line = null;
            //把文件里面的数据一行一行的读出来
            while ((line = reader.readLine()) != null){
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (stream!= null){
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //返回读出来的数据
        return builder.toString();
    }
}
