package com.ls.voluntaryplatformapp.ui.register;

import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.blankj.utilcode.util.RegexUtils;
import com.ls.libcommon.utils.StatusBar;
import com.ls.libcommon.utils.ToastUtil;
import com.ls.libnetwork.ApiResponse;
import com.ls.libnetwork.ApiService;
import com.ls.libnetwork.JsonCallback;
import com.ls.voluntaryplatformapp.R;
import com.ls.voluntaryplatformapp.databinding.ActivityRegisterBinding;

import java.util.Timer;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding mBinding;

    //屏幕高度
    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        StatusBar.fitSystemBar(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        initScreen();

        initListener();
        listeningEvFocus();
    }

    private void initScreen() {
        //获取屏幕高度
        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight / 3;
    }

    private void initListener() {
        mBinding.registerBtnCode.setOnClickListener(v -> {
            //http://localhost/third/code/email/cgc15388327166@163.com
            if (!listeningEmail(getResources())){
                return;
            }
            String email = mBinding.registerEtEmail.getText().toString().trim();
            mBinding.registerBtnCode.setText("59");
            ApiService.post("/third/code/email/" + email)
                    .execute(new JsonCallback<Object>() {
                        @Override
                        public void onSuccess(ApiResponse<Object> response) {
                            ToastUtil.showToast(getResources().getString(R.string.text_register_code_send_success));
                        }
                    });

        });

        mBinding.registerContainer.addOnLayoutChangeListener(new View.OnLayoutChangeListener(){

            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {//认为软键盘将Activity向上推的高度超过了屏幕高度的1/3，就是软键盘弹起了，这个时候隐藏底部的提交按钮
                    //延迟100ms设置不可见性是避免view还没计算好自己的宽高，设置可见不可见性失效。
                    mBinding.registerBtn.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mBinding.registerBtn.setVisibility(View.GONE);
                        }
                    }, 10);

                } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {//认为软键盘将Activity向下推的高度超过了屏幕高度的1/3，就是软键盘隐藏了，这个时候显示底部的提交按钮
                    mBinding.registerBtn.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mBinding.registerBtn.setVisibility(View.VISIBLE);
                        }
                    }, 10);
                }
            }
        });

    }


    private void listeningEvFocus() {
//        mBinding.registerEtEmail.setListener
    }

    private boolean listeningEmail(Resources resources) {
        String email = mBinding.registerEtEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            mBinding.registerEtEmail.setTextColor(resources.getColor(R.color.color_333));
            mBinding.registerEtEmail.setHint(resources.getText(R.string.text_email_waring));
            return false;
        } else if (!RegexUtils.isEmail(email)) {
            mBinding.registerEtEmail.setHint(resources.getString(R.string.text_email_format_error));
            return false;
        }
        return true;
    }


    private boolean listeningEt(Resources resources, String content, EditText et) {
        if (TextUtils.isEmpty(content)) {
            et.setTextColor(resources.getColor(R.color.color_333));
            et.setHint(resources.getText(R.string.text_email_waring));
            return false;
        }
        return true;
    }

    private boolean listeningUserName(Resources resources) {
        String username = mBinding.registerEtUsername.getText().toString();
        if (TextUtils.isEmpty(username)) {
            mBinding.registerEtUsername.setTextColor(resources.getColor(R.color.color_333));
            mBinding.registerEtUsername.setHint(resources.getText(R.string.text_email_waring));
            return false;
        } else if (!RegexUtils.isEmail(username)) {
            mBinding.registerEtEmail.setHint(resources.getString(R.string.text_email_format_error));
            return false;
        }
        return true;
    }


}
