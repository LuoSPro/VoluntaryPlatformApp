package com.ls.voluntaryplatformapp.ui.my;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ls.libcommon.utils.StatusBar;
import com.ls.libnavannotation.FragmentDestination;
import com.ls.voluntaryplatformapp.databinding.FragmentMyBinding;
import com.ls.voluntaryplatformapp.model.Student;
import com.ls.voluntaryplatformapp.model.User;
import com.ls.voluntaryplatformapp.ui.login.UserManager;

@FragmentDestination(pagerUrl = "main/tabs/my",asStarter = true,needLogin = true)
public class MyFragment extends Fragment {

    private FragmentMyBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentMyBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        User user = UserManager.get().getUser();
        mBinding.setStudent((Student) user);

        //当用户信息更新了之后就回调到onChange()方法
        UserManager.get().refresh().observe(getViewLifecycleOwner(), newUser -> {
            if (newUser != null) {
                mBinding.setStudent((Student) newUser);
            }
        });



    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        StatusBar.lightStatusBar(getActivity(), false);
        super.onCreate(savedInstanceState);
    }

    /**
     * 黑底白字
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        StatusBar.lightStatusBar(getActivity(), hidden);
    }


}
