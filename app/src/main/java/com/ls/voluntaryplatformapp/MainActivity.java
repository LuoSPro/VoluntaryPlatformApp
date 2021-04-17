package com.ls.voluntaryplatformapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.os.StrictMode;
import android.os.UserManager;
import android.text.TextUtils;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ls.voluntaryplatformapp.model.Destination;
import com.ls.voluntaryplatformapp.utils.AppConfig;
import com.ls.voluntaryplatformapp.utils.NavGraphBuilder;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private NavController mNavController;
    private BottomNavigationView mNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavView = findViewById(R.id.nav_view);


        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);

        //这样，我们就不用布局文件中的：app:navGraph="@navigation/mobile_navigation" 了
        NavGraphBuilder.build(mNavController, this, fragment.getId());

        //设置底部导航栏的监听事件
        mNavView.setOnNavigationItemSelectedListener(this);

//        GetRequest<JSONObject> request = new GetRequest<>("https://www.baidu.com/");
        //同步

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        //切换tab前，先判断用户是否登录，没登录，就拉起登录页
        HashMap<String, Destination> destination = AppConfig.getDestConfig();
        Iterator<Map.Entry<String, Destination>> iterator = destination.entrySet().iterator();
        while (iterator.hasNext()) {//遍历每个节点
            Map.Entry<String, Destination> entry = iterator.next();
            Destination value = entry.getValue();
//            if (value != null && !UserManager.get().isLogin() && value.isNeedLogin() && value.getId() == item.getItemId()) {
//                //如果满足用户没登录，且这个页面需要登录。就执行登录操作
//                UserManager.get().login(this).observe(this, new Observer<User>() {
//                    @Override
//                    public void onChanged(User user) {
//                        //登录成功，继续刚才拦截的跳转
//                        mNavView.setSelectedItemId(item.getItemId());
//                    }
//                });
//                //登录成功，循环结束
//                return false;
//            }
        }

//        //通过NavController跳转页面
//        mNavController.navigate(item.getItemId());
//        //返回true表示被选中，就会有一个上下浮动的效果
//        //根据item的title是否为null，来决定返回值
//        return TextUtils.isEmpty(item.getTitle());
        return true;
    }
}