package com.huyu.psxt.Activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.guoxiaoxing.phoenix.core.model.MediaEntity;
import com.guoxiaoxing.phoenix.picker.Phoenix;
import com.huyu.psxt.R;
import com.huyu.psxt.Tools.FragmentTools;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotographActivity extends BaseActivity {
    private FragmentTools fragmentTools;
    private FragmentManager fragmentManager;
    private int REQUEST_CODE = 200;
    private static final String TAG = "PhotographActivity";
    @BindView(R.id.fragment_wrapper)
    FrameLayout fragment_wrapper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photograph);
        ButterKnife.bind(this);
        initData();
//        Phoenix.with()
//                .theme(PhoenixOption.THEME_DEFAULT)
//                .start(PhotographActivity.this, PhoenixOption.TYPE_TAKE_PICTURE, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            //返回的数据
            List<MediaEntity> result = Phoenix.result(data);
//            mMediaAdapter.setData(result);
            Log.e(TAG, "onActivityResult: "+result.size());
        }
    }

    private void initData(){
        if (fragmentManager == null){
            fragmentManager = getSupportFragmentManager();
        }
        if (fragmentTools == null){
            fragmentTools = new FragmentTools(this,fragmentManager);
        }
    }
}
