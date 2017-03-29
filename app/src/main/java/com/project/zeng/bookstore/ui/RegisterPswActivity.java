package com.project.zeng.bookstore.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.zeng.bookstore.R;
import com.project.zeng.bookstore.entities.Result;
import com.project.zeng.bookstore.entities.User;
import com.project.zeng.bookstore.listeners.DataListener;
import com.project.zeng.bookstore.listeners.TextChangeListener;
import com.project.zeng.bookstore.net.UserAPI;
import com.project.zeng.bookstore.net.impl.UserAPIImpl;

/**
 * Created by zeng on 2017/3/13.
 */

public class RegisterPswActivity extends Activity implements OnClickListener{

    //组件
    private ImageView mBackImgView;

    private EditText mPasswordEtView;
    private ImageView mClearPswImgView;
    private ImageView mShowPswImgView;

    private Button mFinishBtn;

    private boolean isHide = true;//是否隐藏密码，默认隐藏

    private String id;//账号或手机号

    //操作用户的网络API
    UserAPI mUserAPI = new UserAPIImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register_password);
        init();
        initView();
        id = getIntent().getStringExtra("id");
    }

    /**
     * 初始化组件
     */
    private void init(){
        mBackImgView = (ImageView)findViewById(R.id.iv_register_psw_back);
        mPasswordEtView = (EditText)findViewById(R.id.et_login_password);
        mClearPswImgView = (ImageView)findViewById(R.id.iv_clear_password);
        mShowPswImgView = (ImageView)findViewById(R.id.iv_show_password);
        mFinishBtn = (Button)findViewById(R.id.btn_finish);
    }

    /**
     * 初始化事件
     */
    private void initView(){
        mBackImgView.setOnClickListener(this);
        mPasswordEtView.addTextChangedListener(new TextChangeListener(mClearPswImgView, 20));//20:密码最大的位数
        mClearPswImgView.setOnClickListener(this);
        mShowPswImgView.setOnClickListener(this);
        mFinishBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //返回
            case R.id.iv_register_psw_back://-------------可添加退出注册提醒功能
                finish();
                break;
            //设置密码为空
            case R.id.iv_clear_password:
                mPasswordEtView.setText("");
                break;
            case R.id.iv_show_password:
                if(isHide){
                    //显示密码
                    mPasswordEtView.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    isHide = false;
                }else{
                    //默认隐藏密码
                    mPasswordEtView.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    isHide = true;
                }
                break;
            case R.id.btn_finish:
                String password = mPasswordEtView.getText().toString().trim();
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(this, "请输入密码!", Toast.LENGTH_SHORT).show();
                    break;
                }else if(password.length() < 6){
                    Toast.makeText(this, "密码不能少于6位!", Toast.LENGTH_SHORT).show();
                    break;
                }
                mUserAPI.registerUser(new User(id, password), new DataListener<Result>() {
                    @Override
                    public void onComplete(Result result) {
//                        Log.e("RegisterPswActivity", "result=" + result);
                        if(result.getResult().contains("success")){
                            Toast.makeText(RegisterPswActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            //跳转到登录界面
                            Intent intent = new Intent(RegisterPswActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
                break;
        }
    }
}
