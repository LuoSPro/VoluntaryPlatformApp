package com.ls.voluntaryplatformapp.ui.action;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.ls.voluntaryplatformapp.model.Action;
import com.ls.voluntaryplatformapp.ui.AbsListFragment;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

public class ActionPagerFragment extends AbsListFragment<Action,ActionPagerViewModel> {

    public static ActionPagerFragment newInstance(String feedType) {
        Bundle args = new Bundle();
        args.putString("feedType",feedType);
        ActionPagerFragment fragment = new ActionPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public PagedListAdapter getAdapter() {
        return new ActionPagerAdapter(getContext(),"");
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {

    }
}
