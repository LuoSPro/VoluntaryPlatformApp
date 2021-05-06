package com.ls.voluntaryplatformapp.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ls.voluntaryplatformapp.databinding.ItemArticleListBinding;
import com.ls.voluntaryplatformapp.model.Article;
import com.ls.voluntaryplatformapp.ui.detail.ArticleDetailActivity;

public class ArticleAdapter extends PagedListAdapter<Article,ArticleAdapter.ViewHolder> {
    private final LayoutInflater mInflater;
    private Context mContext;

    protected ArticleAdapter(Context context) {
        super(new DiffUtil.ItemCallback<Article>() {
            @Override
            public boolean areItemsTheSame(@NonNull Article oldItem, @NonNull Article newItem) {
                //判断两个Item是否相同
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Article oldItem, @NonNull Article newItem) {
                //判断两个Item的内容是否相同
                return oldItem.equals(newItem);
            }
        });
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemArticleListBinding binding = ItemArticleListBinding.inflate(mInflater);
        return new ViewHolder(binding.getRoot(),binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(getItem(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArticleDetailActivity.startFeedDetailActivity(mContext,getItem(position));
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding mBinding;

        public ViewHolder(@NonNull View itemView, ViewDataBinding binding) {
            super(itemView);
            mBinding = binding;
        }

        public void bindData(Article article){
            ItemArticleListBinding binding = (ItemArticleListBinding)mBinding;
            binding.setArticle(article);
        }

    }
}
