package com.project.zeng.bookstore.ui.frgm;

import android.app.Fragment;
import android.content.Context;
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
import com.project.zeng.bookstore.adapter.OrderPagerAdapter;
import com.project.zeng.bookstore.db.AbsDBAPI;
import com.project.zeng.bookstore.db.models.DbFactory;
import com.project.zeng.bookstore.entities.User;
import com.project.zeng.bookstore.listeners.DataListener;
import com.project.zeng.bookstore.net.UserAPI;
import com.project.zeng.bookstore.net.impl.UserAPIImpl;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by zeng on 2017/2/28.
 * 我的Fragment
 */

public class MeFragment extends Fragment implements OnClickListener{

    private Context mContext;

    private ViewHolder mViewHolder;
    //适配器
    private OrderPagerAdapter mPagerAdapter;
    //用户的网络请求API
    UserAPI mUserAPI = new UserAPIImpl();
    //操作用户的数据库API
    AbsDBAPI<User> mUserDBAPI = DbFactory.createUserModel();
    private User mUser;

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
        mPagerAdapter = new OrderPagerAdapter(this, view);
        mViewHolder = new ViewHolder(view);
    }

    /**
     * 初始化事件及其适配器
     */
    private void initListener(){
        mViewHolder.mOrderPager.setAdapter(mPagerAdapter);
        mViewHolder.mSettingImgView.setOnClickListener(this);
    }

    /**
     * 获得屏幕宽度
     */
    public int getScreenWidth(){
        return getActivity().getWindowManager().getDefaultDisplay().getWidth();
    }

    /**
     * 获得User的数据 ---------应修改为从数据库加载数据----数据库的用户数据已从登陆的时候加载
     */
    public void fetchData(){
        mUserAPI.fetchUserById("201330350225", new DataListener<User>() {
            @Override
            public void onComplete(User result) {
                if(result != null){
                    mUser = result;
                    Picasso.with(mContext).load(mUser.getPictureUrl()).fit().into(mViewHolder.mUserImgView);
                    mViewHolder.mUserIdView.setText(mUser.getId());
                    mViewHolder.mUserNameView.setText(mUser.getUsername());
                    //将用户信息添加到数据库
                    mUserDBAPI.saveItem(result);
                }
            }
        });
//        mUserDBAPI.loadDatasFromDb(new DataListener<List<User>>() {
//            @Override
//            public void onComplete(List<User> result) {
//                if(result != null){
//                    mUser = result.get(0);
//                }
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //设置按钮
            case R.id.iv_me_setting:

                break;
        }
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
