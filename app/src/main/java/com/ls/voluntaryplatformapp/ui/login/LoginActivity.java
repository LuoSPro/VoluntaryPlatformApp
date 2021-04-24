package com.ls.voluntaryplatformapp.ui.login;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ViewDataBinding;

import com.ls.libnetwork.ApiResponse;
import com.ls.libnetwork.ApiService;
import com.ls.libnetwork.JsonCallback;
import com.ls.voluntaryplatformapp.R;
import com.ls.voluntaryplatformapp.model.User;
import com.ls.voluntaryplatformapp.utils.StatusBar;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";

    private ViewDataBinding mBinding;
    private EditText mEtAccount;
    private EditText mEtPw;
    private Button mBtnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StatusBar.fitSystemBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEtAccount = findViewById(R.id.et_account);
        mEtPw = findViewById(R.id.et_pw);
        mBtnLogin = findViewById(R.id.btn_login);

        mBtnLogin.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_login){
            String account = mEtAccount.getText().toString();
            String password = mEtPw.getText().toString();
            login(account,password);
        }
    }

    private void login(String account, String password) {
        ApiService.post("/school/login")
                .addParam("account",account)
                .addParam("password",password)
                .execute(new JsonCallback<User>() {
                    @Override
                    public void onSuccess(ApiResponse<User> response) {
                        if (response.body != null){
                            UserManager.get().save(response.body);
                            finish();
                        }else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),"登录失败,msg:"+response.message,Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(ApiResponse<User> response) {
                        super.onError(response);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "登陆失败,msg:" + response.message, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onCacheSuccess(ApiResponse<User> response) {
                        super.onCacheSuccess(response);
                    }
                });
        finish();
    }

}
