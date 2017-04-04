package com.project.zeng.bookstore.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.de.hdodenhof.circleimageview.CircleImageView;
import com.example.zeng.bookstore.R;
import com.project.zeng.bookstore.MyApplication;
import com.project.zeng.bookstore.entities.Result;
import com.project.zeng.bookstore.entities.User;
import com.project.zeng.bookstore.listeners.DataListener;
import com.project.zeng.bookstore.net.UserAPI;
import com.project.zeng.bookstore.net.impl.UserAPIImpl;
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
    //全部变量
    private MyApplication app;
    //用户的网络请求API
    UserAPI mUserAPI = new UserAPIImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_settting);
        app = (MyApplication) getApplication();
        mUser = app.getUser();
        init();
        initView();
        initListener();
    }

    /**
     * 初始化组件
     */
    private void init(){
        mViewHolder = new ViewHolder(this);
    }

    /**
     * 初始化界面
     */
    private void initView(){
        if(null == mUser){//未登录
            mViewHolder.mUserLayout.setVisibility(GONE);
            mViewHolder.mLocationLayout.setVisibility(View.GONE);
            mViewHolder.mChangePswLayout.setVisibility(View.GONE);
            mViewHolder.mStoreLayout.setVisibility(View.GONE);
            mViewHolder.mOrderLayout.setVisibility(View.GONE);
            mViewHolder.mLogoutView.setVisibility(View.GONE);
        }else{//已登录
            mViewHolder.mUserLayout.setVisibility(View.VISIBLE);
            mViewHolder.mLocationLayout.setVisibility(View.VISIBLE);
            mViewHolder.mChangePswLayout.setVisibility(View.VISIBLE);
            mViewHolder.mStoreLayout.setVisibility(View.VISIBLE);
            mViewHolder.mOrderLayout.setVisibility(View.VISIBLE);
            mViewHolder.mLogoutView.setVisibility(View.VISIBLE);
            Picasso.with(this).load(mUser.getPictureUrl()).fit().into(mViewHolder.mUserImgView);
            mViewHolder.mUserIdView.setText(mUser.getId());
            mViewHolder.mUserNameView.setText("会员名:" + mUser.getUsername());
        }
    }

    /**
     * 初始化事件及其适配器
     */
    private void initListener(){
        mViewHolder.mBackView.setOnClickListener(this);
        mViewHolder.mUserLayout.setOnClickListener(this);
        mViewHolder.mLocationLayout.setOnClickListener(this);
        mViewHolder.mChangePswLayout.setOnClickListener(this);
        mViewHolder.mStoreLayout.setOnClickListener(this);
        mViewHolder.mOrderLayout.setOnClickListener(this);
        mViewHolder.mLogoutView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //返回按钮
            case R.id.iv_setting_back:
                if(!app.getToken().equals("")){
                    setResult(0);
                }else{
                    setResult(1);
                }
                finish();
                break;
            //用户
            case R.id.rl_setting_user:
//                Toast.makeText(this, "用户", Toast.LENGTH_SHORT).show();
                Intent userIntent = new Intent(this, UserSettingActivity.class);
                startActivityForResult(userIntent, 0);//有返回结果，用于更新界面
                break;
            //我的收货地址
            case R.id.rl_setting_location:
//                Toast.makeText(this, "我的收货地址", Toast.LENGTH_SHORT).show();
                showLocationDialog();
                break;
            //重设密码
            case R.id.rl_setting_change_psw:
//                Toast.makeText(this, "重设密码", Toast.LENGTH_SHORT).show();
                Intent resetPswIntent = new Intent(this, ResetPasswordActivity.class);
                startActivity(resetPswIntent);
                break;
            //我的商店
            case R.id.rl_setting_store:
                Intent storeIntent = new Intent(this, StoreActivity.class);
                startActivity(storeIntent);
                break;
            //我的订单
            case R.id.rl_setting_order:
                Intent orderIntent = new Intent(this, OrderManageActivity.class);
                startActivity(orderIntent);
                break;
            //退出登录
            case R.id.tv_setting_logout:
//                Toast.makeText(this, "退出登录", Toast.LENGTH_SHORT).show();
                app.stopGetTokenService();//停止定时获取令牌的Service
                setResult(1);
                finish();
                break;
        }
    }

    /**
     * 修改收货地址
     */
    private void showLocationDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        final View view = LayoutInflater.from(this).inflate(R.layout.dialog_modify_user_location, null);
        final EditText mNewLocation = (EditText) view.findViewById(R.id.et_user_new_location);
        mNewLocation.setText(mUser.getLocation());
        dialog.setView(view);
        dialog.setTitle("修改收货地址");
        dialog.setPositiveButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String newLocation = mNewLocation.getText().toString().trim();
                if(!newLocation.equals("")){
                    mUserAPI.modifyUserLocation(app.getToken(), newLocation, new DataListener<Result>() {
                        @Override
                        public void onComplete(Result result) {
                            if(result.getResult().contains("success")){
                                mUser.setLocation(newLocation);
                            }
                            Toast.makeText(SettingActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(SettingActivity.this, "请输入新的收货地址!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.setNegativeButton("取消", null);
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 0){
            Picasso.with(this).load(mUser.getPictureUrl()).fit().into(mViewHolder.mUserImgView);//更新用户头像
            mViewHolder.mUserNameView.setText(mUser.getUsername());//更新用户名
            setResult(0);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            mViewHolder.mBackView.performClick();
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
        private RelativeLayout mStoreLayout;
        private RelativeLayout mOrderLayout;
        private TextView mLogoutView;

        public ViewHolder(Activity activity){
            mBackView = (ImageView) activity.findViewById(R.id.iv_setting_back);
            mUserImgView = (CircleImageView) activity.findViewById(R.id.civ_setting_user_img);
            mUserIdView = (TextView) activity.findViewById(R.id.tv_setting_user_id);
            mUserNameView = (TextView) activity.findViewById(R.id.tv_setting_username);
            mUserLayout = (RelativeLayout) activity.findViewById(R.id.rl_setting_user);
            mLocationLayout = (RelativeLayout) activity.findViewById(R.id.rl_setting_location);
            mChangePswLayout = (RelativeLayout) activity.findViewById(R.id.rl_setting_change_psw);
            mStoreLayout = (RelativeLayout) activity.findViewById(R.id.rl_setting_store);
            mOrderLayout = (RelativeLayout) activity.findViewById(R.id.rl_setting_order);
            mLogoutView = (TextView) activity.findViewById(R.id.tv_setting_logout);
        }
    }
}
