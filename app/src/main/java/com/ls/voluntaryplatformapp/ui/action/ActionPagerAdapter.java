package com.ls.voluntaryplatformapp.ui.action;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ls.voluntaryplatformapp.model.Action;

public class ActionPagerAdapter extends PagedListAdapter<Action,ActionPagerAdapter.ViewHolder> {

    private final LayoutInflater mInflater;
    private Context mContext;
    private String mCategory;

    protected ActionPagerAdapter(Context context,String category) {
        super(new DiffUtil.ItemCallback<Action>() {
            @Override
            public boolean areItemsTheSame(@NonNull Action oldItem, @NonNull Action newItem) {
                //判断两个Item是否相同
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Action oldItem, @NonNull Action newItem) {
                //判断两个Item的内容是否相同
                return oldItem.equals(newItem);
            }
        });
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mCategory = category;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
