package com.project.zeng.bookstore.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.de.hdodenhof.circleimageview.CircleImageView;
import com.example.zeng.bookstore.R;
import com.project.zeng.bookstore.entities.User;
import com.squareup.picasso.Picasso;

import static android.view.View.*;

/**
 * Created by zeng on 2017/3/23.
 * 设置Activity
 */

public class SettingActivity extends Activity implements OnClickListener{

    //ViewHolder
    private ViewHolder mViewHolder;
    //当前的用户
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_settting);
        init();
        initListener();
    }

    /**
     * 初始化组件
     */
    private void init(){
        mViewHolder = new ViewHolder(this);
        Bundle bundle = getIntent().getExtras();
        mUser = (User) bundle.getSerializable("user");
        Picasso.with(this).load(mUser.getPictureUrl()).fit().into(mViewHolder.mUserImgView);
        mViewHolder.mUserIdView.setText(mUser.getId());
        mViewHolder.mUserNameView.setText("会员名:" + mUser.getUsername());
    }

    /**
     * 初始化事件及其适配器
     */
    private void initListener(){
        mViewHolder.mBackView.setOnClickListener(this);
        mViewHolder.mUserLayout.setOnClickListener(this);
        mViewHolder.mLocationLayout.setOnClickListener(this);
        mViewHolder.mChangePswLayout.setOnClickListener(this);
        mViewHolder.mLogoutView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //返回按钮
            case R.id.iv_setting_back:
                finish();
                break;
            //用户
            case R.id.rl_setting_user:
//                Toast.makeText(this, "用户", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, UserSettingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", mUser);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            //我的收货地址
            case R.id.rl_setting_location:
                Toast.makeText(this, "我的收货地址", Toast.LENGTH_SHORT).show();
                break;
            //修改密码
            case R.id.rl_setting_change_psw:
                Toast.makeText(this, "修改密码", Toast.LENGTH_SHORT).show();
                break;
            //退出登录
            case R.id.tv_setting_logout:
                Toast.makeText(this, "退出登录", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * SettingActivity的ViewHolder
     */
    private static class ViewHolder{
        private ImageView mBackView;
        private CircleImageView mUserImgView;
        private TextView mUserIdView;
        private TextView mUserNameView;

        private RelativeLayout mUserLayout;
        private RelativeLayout mLocationLayout;
        private RelativeLayout mChangePswLayout;
        private TextView mLogoutView;

        public ViewHolder(Activity activity){
            mBackView = (ImageView) activity.findViewById(R.id.iv_setting_back);
            mUserImgView = (CircleImageView) activity.findViewById(R.id.civ_setting_user_img);
            mUserIdView = (TextView) activity.findViewById(R.id.tv_setting_user_id);
            mUserNameView = (TextView) activity.findViewById(R.id.tv_setting_username);
            mUserLayout = (RelativeLayout) activity.findViewById(R.id.rl_setting_user);
            mLocationLayout = (RelativeLayout) activity.findViewById(R.id.rl_setting_location);
            mChangePswLayout = (RelativeLayout) activity.findViewById(R.id.rl_setting_change_psw);
            mLogoutView = (TextView) activity.findViewById(R.id.tv_setting_logout);
        }
    }
}
