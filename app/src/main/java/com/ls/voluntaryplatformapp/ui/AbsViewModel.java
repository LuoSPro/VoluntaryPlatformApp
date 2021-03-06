package com.ls.voluntaryplatformapp.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

/**
 * 作为一个模板，把每个ViewModel的工作一起做了
 * 比如把PageList先配置了，同时暴露接口给外面
 * @param <T>
 */
public abstract class AbsViewModel<T> extends ViewModel {

    private DataSource mDataSource;
    private final LiveData<PagedList<T>> mPageData;

    //创建一个LiveData，方便我们在BoundaryCallback的回调方法中回调
    private MutableLiveData<Boolean> mBooleanMutableLiveData =new MutableLiveData<>();
    protected final PagedList.Config mConfig;

    public AbsViewModel() {

        mConfig = new PagedList.Config.Builder()
                .setPageSize(10)//每次分页时需要加载的数量
                .setInitialLoadSizeHint(12)//初始化时，第一次加载数据的数量
//                .setMaxSize(100)//一般我们都不知道有多少条数据，所以这个api不常用
//                .setEnablePlaceholders(false)//占位符,如果我们设置最大为100条数据，第一次加载时请求了10条，那么我们滑到第11条的时候，如果数据还没请求到，就会使用占位符来代替
                .setPrefetchDistance(0)//距离屏幕下边缘还有几个item的时候开始加载下一页(设置为0，解决只有一条数据时，多次请求后台的情况)
                .build();
        mPageData = new LivePagedListBuilder(mFactory, mConfig)
                .setInitialLoadKey(0)//加载初始化数据的时候需要传递的参数;
//                .setFetchExecutor()//给PageList提供一个执行异步操作的线程池。Paging里面一般有内置的，不用调用
                .setBoundaryCallback(mCallback)//这个callback能监听到PageList数据加载的一个状态
                .build();

        //通过调用这个方法就能触发Paging框架去加载数据
//        mPageData.observe();
    }

    /**
     * 暴露接口给外面
     * @return
     */
    public LiveData<PagedList<T>> getPageData(){
        return mPageData;
    }

    /**
     * 暴露接口给外面
     * @return
     */
    public DataSource getDataSource(){
        return mDataSource;
    }

    public MutableLiveData<Boolean> getBooleanMutableLiveData() {
        return mBooleanMutableLiveData;
    }

    /**
     * 监听PageList加载数据时的状态
     */
    PagedList.BoundaryCallback<T> mCallback = new PagedList.BoundaryCallback<T>() {
        @Override
        public void onZeroItemsLoaded() {//加载数据时，返回空值
            mBooleanMutableLiveData.postValue(false);//给外界回调
        }

        @Override
        public void onItemAtFrontLoaded(@NonNull T itemAtFront) {//列表的第一条数据被加载的时候
            mBooleanMutableLiveData.postValue(true);
        }

        @Override
        public void onItemAtEndLoaded(@NonNull T itemAtEnd) {//pageList的最后一条数据被加载到的时候
            super.onItemAtEndLoaded(itemAtEnd);
        }
    };

    /**
     * 构建LiveData时需要这个对象作为参数
     */
    DataSource.Factory mFactory = new DataSource.Factory(){
        @Override
        public DataSource create() {//这个方法让子类去实现
            if (mDataSource == null || mDataSource.isInvalid()) {
                mDataSource = createDataSource();
            }
            return mDataSource;
        }
    };

    public abstract DataSource createDataSource();
}
