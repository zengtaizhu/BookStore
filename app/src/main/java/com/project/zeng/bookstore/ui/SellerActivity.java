package com.project.zeng.bookstore.ui;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zeng.bookstore.R;
import com.project.zeng.bookstore.entities.User;
import com.project.zeng.bookstore.listeners.DataListener;
import com.project.zeng.bookstore.net.UserAPI;
import com.project.zeng.bookstore.net.impl.UserAPIImpl;
import com.squareup.picasso.Picasso;

/**
 * Created by zeng on 2017/3/31.
 * 卖家的用户信息的Activity
 */

public class SellerActivity extends Activity implements View.OnClickListener{

    //组件
    private ImageView mBackView;
    private ImageView mSellerImgView;
    private TextView mSellerIdView;
    private TextView mSellerNameView;
    private TextView mSellerGradeView;
    private TextView mSellerMajorView;
    private TextView mSellerPhoneView;
    private TextView mSellerLocationView;
    private TextView mSellerMoreView;

    //用户信息的网络请求API
    private UserAPI mUserApI = new UserAPIImpl();

    private User mSeller;//卖家

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_seller);
        String sellerId = getIntent().getStringExtra("sellerId");
        fetchData(sellerId);
        init();
        mBackView.setOnClickListener(this);
    }

    /**
     * 初始化组件
     */
    private void init(){
        mBackView = (ImageView) findViewById(R.id.iv_seller_back);
        mSellerImgView = (ImageView) findViewById(R.id.iv_seller_img);
        mSellerIdView = (TextView) findViewById(R.id.tv_seller_id);
        mSellerNameView = (TextView) findViewById(R.id.tv_seller_name);
        mSellerGradeView = (TextView) findViewById(R.id.tv_seller_grade);
        mSellerMajorView = (TextView) findViewById(R.id.tv_seller_major);
        mSellerPhoneView = (TextView) findViewById(R.id.tv_seller_phone);
        mSellerLocationView = (TextView) findViewById(R.id.tv_seller_location);
        mSellerMoreView = (TextView) findViewById(R.id.tv_seller_more);
    }

    /**
     * 获取卖家信息
     */
    private void fetchData(String sellerId){
        mUserApI.fetchUserById(sellerId, new DataListener<User>() {
            @Override
            public void onComplete(User result) {
                if(null != result){
                    mSeller = result;
                    initView();
                }
            }
        });
    }

    /**
     * 初始化界面
     */
    private void initView(){
        Picasso.with(this).load(mSeller.getPictureUrl()).fit().into(mSellerImgView);
        mSellerIdView.setText(mSeller.getId());
        mSellerNameView.setText("名字:" + mSeller.getUsername());
        mSellerGradeView.setText("年级:" + mSeller.getGrade());
        mSellerMajorView.setText("专业:" + mSeller.getMajor());
        mSellerPhoneView.setText(mSeller.getPhone());
        mSellerLocationView.setText(mSeller.getLocation());
        mSellerMoreView.setText(mSeller.getMore());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //返回按钮
            case R.id.iv_seller_back:
                finish();
                break;
        }
    }
}
