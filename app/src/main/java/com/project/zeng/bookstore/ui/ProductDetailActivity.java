package com.project.zeng.bookstore.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.example.zeng.bookstore.R;

/**
 * Created by zeng on 2017/3/4.
 * 商品详细Activity
 */

public class ProductDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_product_detail);
    }
}
