package com.project.zeng.bookstore.ui.frgm;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zeng.bookstore.R;
import com.project.zeng.bookstore.adapter.OrderPagerAdapter;

/**
 * Created by zeng on 2017/2/28.
 * 我的Fragment
 */

public class MeFragment extends Fragment {

    private Context mContext;
    //组件
    private ImageView mUserImgView;
    private TextView mUserNameView;
    private ImageView mSettingImgView;

    private ViewPager mOrderPager;

    //适配器
    private OrderPagerAdapter mPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        mContext = getActivity().getApplication();//获得Fragment的Context
        init(view);
        initListener();
        return view;
    }

    /**
     * 初始化组件
     * @param view
     */
    private void init(View view){
        mUserImgView = (ImageView)view.findViewById(R.id.iv_me_user_img);
        mUserNameView = (TextView)view.findViewById(R.id.tv_me_username);
        mSettingImgView = (ImageView)view.findViewById(R.id.iv_me_setting);
        mOrderPager = (ViewPager)view.findViewById(R.id.vp_me_order);
        mPagerAdapter = new OrderPagerAdapter(this, view);
    }

    /**
     * 初始化事件及其适配器
     */
    private void initListener(){
        mOrderPager.setAdapter(mPagerAdapter);
    }

    /**
     * 获得屏幕宽度
     */
    public int getScreenWidth(){
        return getActivity().getWindowManager().getDefaultDisplay().getWidth();
    }
}
