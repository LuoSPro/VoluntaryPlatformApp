package com.ls.voluntaryplatformapp.ui.action;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.ItemKeyedDataSource;
import androidx.paging.PagedList;

import com.alibaba.fastjson.TypeReference;
import com.ls.libnetwork.ApiResponse;
import com.ls.libnetwork.ApiService;
import com.ls.libnetwork.JsonCallback;
import com.ls.libnetwork.Request;
import com.ls.voluntaryplatformapp.model.Action;
import com.ls.voluntaryplatformapp.ui.AbsViewModel;
import com.ls.voluntaryplatformapp.ui.MutableDataSource;
import com.ls.voluntaryplatformapp.ui.login.UserManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ActionPagerViewModel extends AbsViewModel<Action> {

    private static final String TAG = "ActionPagerViewModel";

    //是否加载缓存
    private volatile boolean witchCache = true;

    //这里使用MutableLiveData，就是因为MutableLiveData里面将PostValue和setValue方法设置成了public，便于发送
    private MutableLiveData<PagedList<Action>> cacheLiveData = new MutableLiveData<>();

    //设置同步标志位
    private AtomicBoolean loadAfterFlag = new AtomicBoolean(false);


    @Override
    public DataSource createDataSource() {
        return new ActionDataSource();
    }

    class ActionDataSource extends ItemKeyedDataSource<Integer, Action> {
        @Override
        public void loadInitial(@NonNull ItemKeyedDataSource.LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Action> callback) {
            //加载初始化数据的
            //首页的加载：先加载缓存，然后再加载网络数据，网络数据成功之后，再更新本地缓存
            loadData(0, callback);
            witchCache = false;
        }

        @Override
        public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Action> callback) {
            //加载分页数据的
            loadData(params.key, callback);
        }

        @Override
        public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Action> callback) {
            callback.onResult(Collections.emptyList());
            //是能够向前加载数据的——一般不怎么用到
        }

        @NonNull
        @Override
        public Integer getKey(@NonNull Action item) {
            //通过最后一条Item的信息，去返回一个Item的对象
            return item.getId();
        }
    }

    /**
     * 首页的加载：先加载缓存，然后再加载网络数据，网络数据成功之后，再更新本地缓存
     * @param key
     * @param callback
     */
    private void loadData(int key, ItemKeyedDataSource.LoadCallback<Action> callback) {
        if (key > 0) {//如果传入的key是大于0的，代表此次加载是分页的，我们就要设置同步位为true
            loadAfterFlag.set(true);
        }
        //由于PageList在调用loadInitial、loadAfter、loadBefore方法的时候，已经开了子线程了，所以我们在这里就没必要再开子线程了，所以这里直接使用同步请求
        Request request = ApiService.get("/feeds/queryHotFeedsList")
                .addParam("userId", UserManager.get().getUserId())//用户id
                .addParam("feedId", key)
                .addParam("pageCount", 10)//分页加载多少条
                .responseType(new TypeReference<ArrayList<Action>>() {
                }.getType());//同步方法，需要添加一个responseType。（因为同步无法获取到泛型类型）

        if (witchCache) {//需要加载缓存
            request.cacheStrategy(Request.CACHE_ONLY);//只加载缓存
            request.execute(new JsonCallback<List<Action>>() {
                @Override
                public void onCacheSuccess(ApiResponse<List<Action>> response) {
                    if (response.body != null) {
                        Log.d(TAG, "onCacheSuccess: " + response.body.size());
                        //我们要将请求到的数据通过adapter.subList()设置到Adapter里面去，但是subList接收的是PagedList对象，所以我们需要将body进行转化
                        List<Action> body = response.body;
                        //将body和PagedList进行封装，需要使用自定义的DataSource
                        MutableDataSource dataSource = new MutableDataSource<Integer, Action>();
                        dataSource.data.addAll(body);
                        PagedList pagedList = dataSource.buildNewPagedList(mConfig);//mConfig传进去构造PagedList对象
                        //因为是在子线程里面，所以这里只能使用PostValue()方法
                        cacheLiveData.postValue(pagedList);
                    } else {
                        Log.d(TAG, "onCacheSuccess: response body is null");
                    }
                }
            });
        }

        //网络数据的请求
        try {
            //判断是从缓存拿数据，还是直接去请求网络，如果刚才已经加载了缓存了（witchCache=true），则使用request去clone一个对象
            Request netRequest = witchCache ? request.clone() : request;
            //更改cacheStrategy的值，就可以实现，先读取缓存，载请求数据了
            netRequest.cacheStrategy(key == 0 ? Request.NET_CACHE : Request.NET_ONLY);
            //请求后的返回结果
            ApiResponse<List<Action>> response = netRequest.execute();
            //判断返回的数据是否为null，为null就传进去一个空的list
            List<Action> data = (response == null || response.body == null) ? Collections.emptyList() : response.body;
            //通知请求的结果到了，回调onResult
            callback.onResult(data);
            if (key > 0) {//key>0:上拉加载
                //通过liveData发送数据，告诉UI层 是否应该主动关闭上拉加载分页的动画
                getBooleanMutableLiveData().postValue(data.size() > 0);
                //如果加载完之后，发现key>0，就说明还能加载分页数据，我们设置同步位为false
                loadAfterFlag.set(false);
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        Log.e(TAG, "loadData: key --> " + key);
    }
}
