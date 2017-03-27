package com.project.zeng.bookstore.ui.frgm;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zeng.bookstore.R;
import com.project.zeng.bookstore.MyApplication;
import com.project.zeng.bookstore.adapter.OrderPagerAdapter;
import com.project.zeng.bookstore.db.AbsDBAPI;
import com.project.zeng.bookstore.db.models.DbFactory;
import com.project.zeng.bookstore.entities.User;
import com.project.zeng.bookstore.listeners.DataListener;
import com.project.zeng.bookstore.net.UserAPI;
import com.project.zeng.bookstore.net.impl.UserAPIImpl;
import com.project.zeng.bookstore.ui.LoginActivity;
import com.project.zeng.bookstore.ui.SettingActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by zeng on 2017/2/28.
 * 我的Fragment
 */

public class MeFragment extends Fragment implements OnClickListener{

    private Context mContext;
    private MyApplication app;//全局变量

    private ViewHolder mViewHolder;
    //适配器
    private OrderPagerAdapter mPagerAdapter;
    private User mUser;//当前用户
    private String LOGIN_OR_REGISTER = "登录/注册>";
    private int SETTING_REQUEST_CODE = 0;//SettingActivity的请求码
    private int LOGIN_REQUEST_CODE = 1;//LoginActivity的请求码

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        mContext = getActivity().getApplication();//获得Fragment的Context
        app = (MyApplication) mContext;
        mUser = app.getUser();
        init(view);
        initListener();
        initView();
        return view;
    }

    /**
     * 初始化组件
     * @param view
     */
    private void init(View view){
        mPagerAdapter = new OrderPagerAdapter(this, view);
        mViewHolder = new ViewHolder(view);
    }

    /**
     * 初始化事件及其适配器
     */
    private void initListener(){
        mViewHolder.mOrderPager.setAdapter(mPagerAdapter);
        mViewHolder.mSettingImgView.setOnClickListener(this);
        mViewHolder.mUserNameView.setOnClickListener(this);
    }

    /**
     * 获得屏幕宽度
     */
    public int getScreenWidth(){
        return getActivity().getWindowManager().getDefaultDisplay().getWidth();
    }

    /**
     * 初始化界面
     */
    public void initView(){
        if(null == mUser){
            mViewHolder.mUserNameView.setText(LOGIN_OR_REGISTER);//替换原来的用户名
            mViewHolder.mUserImgView.setImageResource(R.mipmap.ic_user);//使用默认头像
            mViewHolder.mUserIdView.setVisibility(View.INVISIBLE);//隐藏用户ID
        }else{
            Picasso.with(mContext).load(mUser.getPictureUrl()).fit().into(mViewHolder.mUserImgView);
            mViewHolder.mUserIdView.setText(mUser.getId());
            mViewHolder.mUserNameView.setText(mUser.getUsername());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //设置按钮
            case R.id.iv_me_setting:
                Intent settingIntent = new Intent(mContext, SettingActivity.class);
                startActivityForResult(settingIntent, SETTING_REQUEST_CODE);
                break;
            //用户名：若为User为空，则跳转到登录界面
            case R.id.tv_me_username:
                if(mViewHolder.mUserNameView.getText().equals(LOGIN_OR_REGISTER)&&null == app.getUser()){
                    Intent loginIntent = new Intent(mContext, LoginActivity.class);
                    startActivityForResult(loginIntent, LOGIN_REQUEST_CODE);
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == SETTING_REQUEST_CODE){
            if(resultCode == 0){//更新用户头像
                Picasso.with(mContext).load(mUser.getPictureUrl()).fit().into(mViewHolder.mUserImgView);
                mViewHolder.mUserNameView.setText(mUser.getUsername());
            }
            if(resultCode == 1){
                app.setUser(null);//清空User
                app.setToken("");//将令牌设置为空
                mUser = null;
                initView();
                initOrderPager();
            }
        }
        if(requestCode == LOGIN_REQUEST_CODE){
            if(resultCode == 0){//登录成功
                mUser = app.getUser();
                mViewHolder.mUserIdView.setVisibility(View.VISIBLE);//显示用户ID
                initView();
                initOrderPager();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 初始化订单ViewPager，刷新ViewPager的Fragment
     */
    private void initOrderPager(){
        mPagerAdapter.updateView();//刷新ViewPager的Fragment
        mViewHolder.mOrderPager.setCurrentItem(0);
        mViewHolder.mOrderPager.setCurrentItem(1);
        mViewHolder.mOrderPager.setCurrentItem(2);
        mViewHolder.mOrderPager.setCurrentItem(3);
        mViewHolder.mOrderPager.setCurrentItem(OrderPagerAdapter.POSITION_NONE);
    }

    /**
     * MeFragment的ViewHolder
     */
    private static class ViewHolder{
        private ViewPager mOrderPager;
        private ImageView mUserImgView;
        private TextView mUserIdView;
        private TextView mUserNameView;
        private ImageView mSettingImgView;

        public ViewHolder(View view) {
            mOrderPager = (ViewPager)view.findViewById(R.id.vp_me_order);
            mUserImgView = (ImageView)view.findViewById(R.id.iv_me_user_img);
            mUserIdView = (TextView)view.findViewById(R.id.tv_me_user_id);
            mUserNameView = (TextView)view.findViewById(R.id.tv_me_username);
            mSettingImgView = (ImageView)view.findViewById(R.id.iv_me_setting);
        }
    }
}
