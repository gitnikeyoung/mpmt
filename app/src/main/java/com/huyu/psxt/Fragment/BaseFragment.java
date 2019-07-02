package com.huyu.psxt.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huyu.psxt.Activity.PhotographActivity;

import butterknife.ButterKnife;
import me.jessyan.autosize.AutoSizeConfig;

public class BaseFragment extends Fragment {
    private PhotographActivity photographActivity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    private  void initPub(View view){
        ButterKnife.bind(this,view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        AutoSizeConfig.getInstance().setCustomFragment(true);
    }

    private  void initFragment1(View view){
        initPub(view);
        photographActivity = (PhotographActivity) getActivity();
    }
}
