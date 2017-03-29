package com.project.zeng.bookstore.ui;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.Window;

import com.example.zeng.bookstore.R;
import com.project.zeng.bookstore.MyApplication;
import com.project.zeng.bookstore.ui.frgm.CartFragment;
import com.project.zeng.bookstore.ui.frgm.HomeFragment;

/**
 * Created by zeng on 2017/3/15.
 * 购物车的Activity
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class CartActivity extends Activity {

    //Fragment布局管理器
    private FragmentManager fm;
    private MyApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_cart);
        app = (MyApplication) getApplication();
        fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        CartFragment cartFragment = new CartFragment();
        cartFragment.showBackImageView(true);//显示返回按钮
        ft.add(R.id.fl_cart_container,cartFragment);
        ft.commit();
        fm.executePendingTransactions();//立刻执行添加Fragment的事务
        cartFragment.fetchData(app.getToken());
    }
}
