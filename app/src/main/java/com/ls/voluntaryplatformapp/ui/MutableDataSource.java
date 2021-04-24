package com.ls.voluntaryplatformapp.ui;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 为了确保缓存数据更新到列表上面，需要在获取换成功之后，将其的response.body转换成Adapter的subList()方法中需要的PagedList对象
 * 所以我们需要将 response.body  -->  DataSource  -->   PagedList
 * @param <Key>
 * @param <Value>
 */
public class MutableDataSource<Key,Value> extends PageKeyedDataSource<Key,Value> {

    public List<Value> data = new ArrayList<>();

    /**
     * 将DataSource转换成PagedList对象，方便外面直接使用，即提供adapter.subList()方法所需的参数
     * @param config
     * @return
     */
    public PagedList<Value> buildNewPagedList(PagedList.Config config){
        @SuppressLint("RestrictedApi")
        PagedList<Value> pagedList = new PagedList.Builder<Key, Value>(this, config)
                .setFetchExecutor(ArchTaskExecutor.getIOThreadExecutor())
                .setNotifyExecutor(ArchTaskExecutor.getMainThreadExecutor())
                .build();
        return pagedList;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Key> params, @NonNull LoadInitialCallback<Key, Value> callback) {
        callback.onResult(data,null,null);//这里我们不指望他分页，所以传入null就行了
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Key> params, @NonNull LoadCallback<Key, Value> callback) {
        //一般不需要这个方法
        callback.onResult(Collections.emptyList(),null);
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Key> params, @NonNull LoadCallback<Key, Value> callback) {
        //这个我们也不需要
        callback.onResult(Collections.emptyList(),null);
    }
}
