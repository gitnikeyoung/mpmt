package com.huyu.psxt.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.gyf.barlibrary.ImmersionBar;
import com.huyu.psxt.R;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    public void setDeacultBar(int color, Boolean islight, View view){
        ImmersionBar.with(this)
                .statusBarColor(color)//状态栏颜色
                .statusBarDarkFont(islight)//图标颜色，false白色，true黑色
                .titleBar(view)//状态栏占位组件
                .init();
    }
}
