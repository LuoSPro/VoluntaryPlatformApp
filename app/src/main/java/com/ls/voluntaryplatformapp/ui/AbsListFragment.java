package com.ls.voluntaryplatformapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ls.libcommon.view.EmptyView;
import com.ls.voluntaryplatformapp.R;
import com.ls.voluntaryplatformapp.databinding.LayoutRefreshViewBinding;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 每个页面都需要有主动触发初始化数据加载的效果，所以这个触发逻辑就放在AbsFragment中来做
 * 这里接收的泛型参数M就是用来保存子类的ViewModel，然后解析之后保存，方便回调子类里面的PageData
 * @param <T>
 * @param <M>
 */
public abstract class AbsListFragment<T,M extends AbsViewModel<T>> extends Fragment implements OnRefreshListener, OnLoadMoreListener {
    private static final String TAG = "AbsListFragment";

    protected LayoutRefreshViewBinding binding;
    //refreshView
    protected SmartRefreshLayout mRefreshLayout;
    //recyclerView
    protected RecyclerView mRecyclerView;
    //emptyView
    protected EmptyView mEmptyView;
    protected PagedListAdapter<T, RecyclerView.ViewHolder> mAdapter;
    protected M mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = LayoutRefreshViewBinding.inflate(inflater, container, false);
        mRecyclerView = binding.recyclerView;
        mRefreshLayout = binding.refreshLayout;
        mEmptyView = binding.emptyView;

        //设置Refresh的属性
        mRefreshLayout.setEnableRefresh(true);
        mRefreshLayout.setEnableLoadMore(true);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);

        //设置RecyclerView
        mAdapter = getAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setItemAnimator(null);
        //设置RecyclerView的分割
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        decoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.list_divider));
        mRecyclerView.addItemDecoration(decoration);

        genericViewModel();
        return binding.getRoot();
    }


    /**
     * 每个页面都需要有主动触发初始化数据加载的效果，所以这个触发逻辑就放在AbsFragment中来做
     * 这里做的就是在解析子类传过来的泛型参数，把他解析成子类ViewModel对象，方便后面去调用子类ViewModel中的PageData
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void genericViewModel() {
        //利用 子类传递的 泛型参数实例化出absViewModel 对象。
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        Type[] arguments = type.getActualTypeArguments();
        if (arguments.length > 1) {
            Type argument = arguments[1];
            Class modelClaz = ((Class) argument).asSubclass(AbsViewModel.class);
            mViewModel = (M) new ViewModelProvider(this).get(modelClaz);

            //触发页面初始化数据加载的逻辑
            mViewModel.getPageData().observe(getViewLifecycleOwner(), pagedList -> submitList(pagedList));

            //监听分页时有无更多数据,以决定是否关闭上拉加载的动画
            mViewModel.getBooleanMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean hasData) {//监听页面是否有数据
                    finishRefresh(hasData);
                }
            });
//            mViewModel.getBoundaryPageData().observe(this, hasData -> finishRefresh(hasData));
        }
    }

    /**
     *
     * @param pagedList 每次刷新返回的List对象
     */
    public void submitList(PagedList<T> pagedList) {
        if (pagedList.size() > 0) {//将返回的数据添加到Adapter里
            mAdapter.submitList(pagedList);
        }
        finishRefresh(pagedList.size() > 0);
    }

    /**
     * @param hasData 本次加载是否有数据
     */
    public void finishRefresh(boolean hasData) {
        PagedList<T> currentList = mAdapter.getCurrentList();
        hasData = hasData || currentList != null && currentList.size() > 0;
        //RefreshView的当前状态
        RefreshState state = mRefreshLayout.getState();
        if (state.isFooter && state.isOpening) {
            mRefreshLayout.finishLoadMore();
        } else if (state.isHeader && state.isOpening) {
            mRefreshLayout.finishRefresh();
        }
        //设置EmptyView显示和隐藏
        if (hasData) {
            mEmptyView.setVisibility(View.GONE);
        } else {
            mEmptyView.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 因为涉及到分页加载，所以这里要使用 PagedListAdapter
     */
    public abstract PagedListAdapter<T, RecyclerView.ViewHolder> getAdapter();

    /**
     * 将根View传出去，方便外界做界面合并
     */
    public View getRootView(){
        return binding.getRoot();
    }
}
