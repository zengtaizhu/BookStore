package com.project.zeng.bookstore.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zeng.bookstore.R;

import java.util.List;

/**
 * Created by zeng on 2017/4/2.
 * 订单的Activity
 */

public class OrderActivity extends Activity {

    //组件
    private ImageView mBackView;
    private TextView mDeliverView;
    private TextView mReceiveView;
    private TextView mCommentView;
    private TextView mDoneView;
    private ImageView mTabLineView;
    private ViewPager mOrdersPager;

    private List<Fragment> fragmentList;
    private FragmentPagerAdapter adapter;

    private int mScreenOneOfFour = 0;//屏幕的四分之一
    private int currentPageIndex = 0;//当前的页面索引

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_order);
        init();
        initListener();
        initView();
    }

    /**
     * 初始化组件
     */
    private void init(){
        mBackView = (ImageView) findViewById(R.id.iv_order_back);
        mDeliverView = (TextView) findViewById(R.id.tv_order_delivering);
        mReceiveView = (TextView) findViewById(R.id.tv_order_receiving);
        mCommentView = (TextView) findViewById(R.id.tv_order_commenting);
        mDoneView = (TextView) findViewById(R.id.tv_order_done);
        mTabLineView = (ImageView) findViewById(R.id.iv_order_line);
        mOrdersPager = (ViewPager) findViewById(R.id.vp_order_orders);

    }

    /**
     * 初始化事件
     */
    private void initListener() {

    }

    /**
     * 初始化界面
     */
    private void initView(){
        mScreenOneOfFour = getWindowManager().getDefaultDisplay().getWidth() / 4;
        ViewGroup.LayoutParams layoutParams = mTabLineView.getLayoutParams();
        layoutParams.width = mScreenOneOfFour;
        mTabLineView.setLayoutParams(layoutParams);//初始化TabLine为屏幕的四分之一
    }
}
