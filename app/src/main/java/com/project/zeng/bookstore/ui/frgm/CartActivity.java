package com.project.zeng.bookstore.ui.frgm;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Window;

import com.example.zeng.bookstore.R;

/**
 * Created by zeng on 2017/3/15.
 * 购物车的Activity
 */

public class CartActivity extends Activity {

    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_cart);
        fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fl_cart_container, new HomeFragment());
    }
}
