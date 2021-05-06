package com.ls.voluntaryplatformapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.ls.libnavannotation.FragmentDestination;
import com.ls.voluntaryplatformapp.databinding.FragmentHomeBinding;
import com.ls.voluntaryplatformapp.databinding.LayoutRefreshViewBinding;

@FragmentDestination(pagerUrl = "main/tabs/home",asStarter = true)
public class HomeFragment extends Fragment {

    private FragmentHomeBinding mBinding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentHomeBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }
}
