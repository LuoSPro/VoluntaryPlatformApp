package com.ls.libcommon.utils;

import android.widget.Toast;

import com.ls.libcommon.global.AppGlobals;

public class ToastUtil {

    private static Toast sToast;

    public static void showToast(String tips){
        if (sToast == null){
            sToast = Toast.makeText(AppGlobals.getApplication(),tips, Toast.LENGTH_SHORT);
        }else{
            sToast.setText(tips);
        }
        sToast.show();

    }
}
