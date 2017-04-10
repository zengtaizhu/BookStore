package com.project.zeng.bookstore.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zeng.bookstore.R;
import com.project.zeng.bookstore.MyApplication;
import com.project.zeng.bookstore.ui.frgm.OrderManageFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zeng on 2017/4/4.
 * 订单管理的Activity
 */

public class OrderManageActivity extends FragmentActivity implements OnClickListener, OnPageChangeListener{

    private MyApplication app;//全局变量
    //组件
    private ImageView mBackView;
    private TextView mAllOrderView;
    private TextView mDeliverOrderView;
    private TextView mDoneOrderView;
    private TextView mReturnOrderView;
    private ImageView mTabLineView;
    private ViewPager mOrderPager;

    private FragmentPagerAdapter mAdapter;//适配器
    private List<OrderManageFragment> mOrderFragments;//订单Fragment列表
    private int mScreenOneOfFour = 0;//屏幕的三分之一
    private int currentPageIndex = 0;//当前的页面索引

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_order_manage);
        app = (MyApplication) getApplication();
        init();
        initListener();
        initView();
    }

    /**
     * 初始化组件
     */
    private void init(){
        mBackView = (ImageView) findViewById(R.id.iv_order_manage_back);
        mAllOrderView = (TextView) findViewById(R.id.tv_order_all);
        mDeliverOrderView = (TextView) findViewById(R.id.tv_order_delivering);
        mDoneOrderView = (TextView) findViewById(R.id.tv_order_done);
        mReturnOrderView = (TextView) findViewById(R.id.tv_order_return);
        mTabLineView = (ImageView) findViewById(R.id.iv_order_line);
        mOrderPager = (ViewPager) findViewById(R.id.vp_order_manage_order);
        mOrderFragments = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            mOrderFragments.add(new OrderManageFragment());
        }
    }

    /**
     * 初始化事件及其适配器
     */
    private void initListener(){
        mBackView.setOnClickListener(this);
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mOrderFragments.get(position);
            }

            @Override
            public int getCount() {
                return mOrderFragments.size();
            }
        };
        mOrderPager.setAdapter(mAdapter);
        mOrderPager.addOnPageChangeListener(this);
    }

    /**
     * 初始化界面
     */
    private void initView(){
        mScreenOneOfFour = getWindowManager().getDefaultDisplay().getWidth() / 4;
        ViewGroup.LayoutParams lp = mTabLineView.getLayoutParams();
        lp.width = mScreenOneOfFour;
        mTabLineView.setLayoutParams(lp);
        mOrderPager.setCurrentItem(0);//设置当前的页面
        mOrderFragments.get(currentPageIndex).fetchData(app.getToken(), currentPageIndex);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //返回按钮
            case R.id.iv_order_manage_back:
                finish();
                break;
            //
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineView.getLayoutParams();
        lp.leftMargin = (int)(currentPageIndex * mScreenOneOfFour + (positionOffset + position - currentPageIndex) * mScreenOneOfFour);
        mTabLineView.setLayoutParams(lp);//改变TabLine的位置
    }

    @Override
    public void onPageSelected(int position) {
        resetOrderState();
        currentPageIndex = position;
        switch (position){
            //全部订单
            case 0:
                mAllOrderView.setTextColor(getResources().getColor(R.color.text_red));
                break;
            //待发货订单
            case 1:
                mDeliverOrderView.setTextColor(getResources().getColor(R.color.text_red));
                break;
            //完成订单
            case 2:
                mDoneOrderView.setTextColor(getResources().getColor(R.color.text_red));
                break;
            //退款订单
            case 3:
                mReturnOrderView.setTextColor(getResources().getColor(R.color.text_red));
        }
        mOrderFragments.get(position).fetchData(app.getToken(), position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 初始化订单的颜色（默认黑色）
     */
    private void resetOrderState(){
        mAllOrderView.setTextColor(getResources().getColor(R.color.text_black));
        mDeliverOrderView.setTextColor(getResources().getColor(R.color.text_black));
        mDoneOrderView.setTextColor(getResources().getColor(R.color.text_black));
        mReturnOrderView.setTextColor(getResources().getColor(R.color.text_black));
    }
}
