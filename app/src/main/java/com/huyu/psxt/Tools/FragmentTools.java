package com.huyu.psxt.Tools;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class FragmentTools {
    //private static FragmentTools _instance;
    private Fragment nowfragment;
    public void setNowfragment(Fragment nowfragment) {
        this.nowfragment = nowfragment;
    }

    public Fragment getNowfragment() {
        return nowfragment;
    }

    private Context context;
    private FragmentManager fragmentManager;
    public FragmentTools(Context mcontext, FragmentManager mfragmentManager){
        context = mcontext;
        fragmentManager = mfragmentManager;
    }
//    public  FragmentTools getInstance(Context context, FragmentManager fragmentManager){
//        if(_instance==null){
//            _instance = new FragmentTools();
//        }
//        _instance.context = context;
//        _instance.fragmentManager = fragmentManager;
//        return _instance;
//    }

    public void addFragment(int resid, Fragment fragment){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(fragment!=null){
            fragmentTransaction.add(resid,fragment);
            fragmentTransaction.commit();
        }
    }

    public void hideFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (fragment!=null&&fragmentManager!=null){
            fragmentTransaction.hide(fragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }


    public void showFragment(Fragment fragment){
        if(fragment!=null&&fragmentManager!=null){
            nowfragment = fragment;
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.show(fragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }
}
