package com.ls.voluntaryplatformapp.ui.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ls.libcommon.global.AppGlobals;
import com.ls.libnetwork.ApiResponse;
import com.ls.libnetwork.ApiService;
import com.ls.libnetwork.JsonCallback;
import com.ls.libnetwork.cache.CacheManager;
import com.ls.voluntaryplatformapp.model.User;

public class UserManager {

    private static final String KEY_CACHE_USER = "cache_user";
    private static final String KEY_CACHE_TOKEN = "cache_token";

    //默认一天
    private static final int sDefaultCacheTime = 1000 * 60 * 60 * 24 ;

    private MutableLiveData<User> mUserMutableLiveData = new MutableLiveData<>();

    private static UserManager mUserManager = new UserManager();
    private User mUser;
    private String mAccessToken;

    /**
     * 暴露接口给外界调用mUserManager
     * @return
     */
    public static UserManager get(){
        return mUserManager;
    }

    private UserManager() {
        //获取我们缓存的user信息
        User userCache = (User) CacheManager.getCache(KEY_CACHE_USER);
        if (userCache != null&&userCache.getExpiresTime() < System.currentTimeMillis()){
            //说明缓存的用户信息还有效
            mUser = userCache;
        }
        String tokenCache = (String) CacheManager.getCache(KEY_CACHE_TOKEN);
        if (tokenCache != null){
            //说明缓存的用户信息还有效
            mAccessToken = tokenCache;
        }
    }

    public String getAccessToken() {
        return mAccessToken;
    }

    public void saveToken(String token) {
        mAccessToken = token;
        //这里传递的对象必须实现了Serializable,因为我们存储数据的时候，是用二进制来保存的
        CacheManager.save(KEY_CACHE_TOKEN, token);
        //以前传递信息的时候，都是在Activity之间使用Intent传递数据，然后在onActivityResult中去获取返回的信息，
        //现在，jetPack中的liveData就能发送数据，通过Observer去观察数据的变化，并且LiveData和生命周期绑定，更安全
    }

    public void save(User user) {
        //保存一份
        mUser = user;
        //手动控制缓存时间
        mUser.setExpiresTime(System.currentTimeMillis() + sDefaultCacheTime);
        //这里传递的对象必须实现了Serializable,因为我们存储数据的时候，是用二进制来保存的
        CacheManager.save(KEY_CACHE_USER, user);
        //以前传递信息的时候，都是在Activity之间使用Intent传递数据，然后在onActivityResult中去获取返回的信息，
        //现在，jetPack中的liveData就能发送数据，通过Observer去观察数据的变化，并且LiveData和生命周期绑定，更安全

        //判断是否有观察者已经注册到LiveData里面了，如果有，我们就不需要再发送这个事件了，因为Observer能够监听到
        if (mUserMutableLiveData.hasObservers()) {//判断是否有observer注册
            mUserMutableLiveData.postValue(user);//调用这个方法的地方可能在主线程，也可能在子线程
        }
    }

    /**
     * 统一跳转LoginActivity的接口
     *
     * @param context
     * @return
     */
    public LiveData<User> login(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        //不设置，会出现：Calling startActivity() from outside of an Activity  context requires the FLAG_ACTIVITY_NEW_TASK flag. Is this really what you want?
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        context.startActivity(intent);
        return mUserMutableLiveData;
    }

    /**
     * 判断用户当前是否登录
     *
     * @return
     */
    public boolean isLogin() {
        return mUser == null ? false : true;
    }

    /**
     * 获取当前登录的用户的信息
     *
     * @return
     */
    public User getUser() {
        return isLogin() ? mUser : null;
    }

    /**
     * 获取用户id
     * @return
     */
    public long getUserId() {
        return isLogin() ? mUser.getId() : 0;
    }

    /**
     * 刷新本地缓存的状态
     * @return
     */
    public LiveData<User> refresh() {
        //需要用户登录
        if (!isLogin()||mAccessToken == null) {
            return login(AppGlobals.getApplication());
        }
        MutableLiveData<User> liveData = new MutableLiveData<>();
        ApiService.get("/student/info")
                .addHeader("Authorization", "Bearer ".concat(mAccessToken))
                .execute(new JsonCallback<User>() {
                    @Override
                    public void onSuccess(ApiResponse<User> response) {
                        save(response.body);
                        liveData.postValue(getUser());
                    }

                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onError(ApiResponse<User> response) {
                        ArchTaskExecutor.getMainThreadExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AppGlobals.getApplication(), response.message, Toast.LENGTH_SHORT).show();
                            }
                        });

                        liveData.postValue(null);
                    }
                });
        return liveData;
    }

    /**
     * 退出当前登录时，把缓存的用户置空
     */
    public void logout() {
        CacheManager.delete(KEY_CACHE_USER, mUser);
        mUser = null;
    }

}
