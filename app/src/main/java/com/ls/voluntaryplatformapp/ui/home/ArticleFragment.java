package com.ls.voluntaryplatformapp.ui.home;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.paging.ItemKeyedDataSource;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.ls.voluntaryplatformapp.model.Article;
import com.ls.voluntaryplatformapp.ui.AbsListFragment;
import com.ls.voluntaryplatformapp.ui.MutableDataSource;
import com.ls.voluntaryplatformapp.ui.action.ActionPagerAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.List;

public class ArticleFragment extends AbsListFragment<Article,HomeViewModel> {

    @Override
    public PagedListAdapter getAdapter() {
        return new ArticleAdapter(getContext());
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        final PagedList<Article> currentList = mAdapter.getCurrentList();
        if (currentList == null || currentList.size() <= 0) {
            finishRefresh(false);
            return;
        }
        //手动处理下拉分页的逻辑
        Article article = mAdapter.getCurrentList().get(mAdapter.getItemCount() - 1);//得到最后一个Item
        mViewModel.loadAfter(article.getId(), new ItemKeyedDataSource.LoadCallback<Article>() {
            @Override
            public void onResult(@NonNull List<Article> data) {
                PagedList.Config config = currentList.getConfig();
                //加载的数据返回回来
                if (data != null && data.size() > 0) {
                    //这里 咱们手动接管 分页数据加载的时候 使用MutableItemKeyedDataSource也是可以的。
                    //由于当且仅当 paging不再帮我们分页的时候，我们才会接管。所以 就不需要ViewModel中创建的DataSource继续工作了，所以使用
                    //MutablePageKeyedDataSource也是可以的
                    MutableDataSource dataSource = new MutableDataSource();
                    dataSource.data.addAll(currentList);
                    dataSource.data.addAll(data);
                    PagedList pagedList = dataSource.buildNewPagedList(config);
                    //给列表添加数据
                    submitList(pagedList);
                }
            }
        });
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        finishRefresh(false);
    }
}
