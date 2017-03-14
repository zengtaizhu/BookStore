package com.project.zeng.bookstore.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Window;

/**
 * Created by zeng on 2017/2/28.
 */

public class BaseActivity extends Activity{

    protected FragmentManager mFrgmManager;
    protected int mFrgmContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获得Fragment管理权
        mFrgmManager = getFragmentManager();
        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    /**
     * 设置Fragment Container
     * @param container
     */
    protected void setFragmentContainer(int container){
        mFrgmContainer = container;
    }

    /**
     * 添加Fragment
     * @param fragment
     */
    protected void addFragment(Fragment fragment){
        mFrgmManager.beginTransaction().add(mFrgmContainer, fragment).commit();
    }

    /**
     * 代替Fragment
     * @param fragment
     */
    protected void replaceFragment(Fragment fragment){
        mFrgmManager.beginTransaction().replace(mFrgmContainer, fragment).commit();
    }
}
