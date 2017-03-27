package com.project.zeng.bookstore.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zeng.bookstore.R;
import com.project.zeng.bookstore.MyApplication;
import com.project.zeng.bookstore.db.AbsDBAPI;
import com.project.zeng.bookstore.db.models.DbFactory;
import com.project.zeng.bookstore.entities.User;
import com.project.zeng.bookstore.listeners.DataListener;
import com.project.zeng.bookstore.listeners.TextChangeListener;
import com.project.zeng.bookstore.net.UserAPI;
import com.project.zeng.bookstore.net.impl.UserAPIImpl;

/**
 * Created by zeng on 2017/3/6.
 * 登录Activity
 */

public class LoginActivity extends Activity implements OnClickListener{

    //组件
    private EditText mAccountEdText;
    private ImageView mClearAccountImgView;

    private EditText mPasswordEdText;
    private ImageView mClearPasswordImgView;
    private ImageView mShowPasswordImgView;

    private Button mLoginBtn;

    private TextView mForgetPasswordTxtView;
    private TextView mRegisterTxtView;

    private boolean isHide = true;//是否隐藏密码，默认隐藏

    //Application，保存全局变量
    private MyApplication app;

    //用户网络请求API
    UserAPI mUserAPI = new UserAPIImpl();
    //操作用户的数据库对象
    AbsDBAPI<User> mUserDbAPI = DbFactory.createUserModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        init();
        initListener();
        app = (MyApplication)getApplication();//获得应用程序MyApplication
    }

    /**
     * 初始化组件
     */
    private void init(){
        mAccountEdText = (EditText)findViewById(R.id.et_login_account);
        mClearAccountImgView = (ImageView)findViewById(R.id.iv_clear_account);
        mPasswordEdText = (EditText)findViewById(R.id.et_login_password);
        mClearPasswordImgView = (ImageView)findViewById(R.id.iv_clear_password);
        mShowPasswordImgView = (ImageView)findViewById(R.id.iv_show_password);
        mLoginBtn = (Button)findViewById(R.id.btn_login);
        mForgetPasswordTxtView = (TextView)findViewById(R.id.tv_forget_password);
        mRegisterTxtView = (TextView)findViewById(R.id.tv_register);
    }

    /**
     * 初始化事件
     */
    private void initListener(){
        mAccountEdText.addTextChangedListener(new TextChangeListener(mClearAccountImgView, 12));//11:手机位数
        mClearAccountImgView.setOnClickListener(this);
        mPasswordEdText.addTextChangedListener(new TextChangeListener(mClearPasswordImgView, 20));//20:密码最大位数
        mClearPasswordImgView.setOnClickListener(this);
        mShowPasswordImgView.setOnClickListener(this);
        mLoginBtn.setOnClickListener(this);
        mForgetPasswordTxtView.setOnClickListener(this);
        mRegisterTxtView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //清除账号输入框的数据
            case R.id.iv_clear_account:
                mAccountEdText.setText("");
                break;
            //清除密码框的数据
            case R.id.iv_clear_password:
                mPasswordEdText.setText("");
                break;
            //将密码以明文的形式显示出来
            case R.id.iv_show_password:
                if(isHide){
                    //显示密码
                    mPasswordEdText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    isHide = false;
                }else{
                    //默认隐藏密码
                    mPasswordEdText.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    isHide = true;
                }
                break;
            //登录按钮
            case R.id.btn_login:
//                Toast.makeText(this, "登录", Toast.LENGTH_SHORT).show();
                String id = mAccountEdText.getText().toString();
                String password = mPasswordEdText.getText().toString();
                if(id.equals("") || password.equals("")){
                    Toast.makeText(this, "请将账号和密码填写完整!!!", Toast.LENGTH_SHORT).show();
                    break;
                }
                //发送账号密码给服务器，获得一小时时限的令牌
                mUserAPI.fetchUserAndToken(new User(id, password), new DataListener<User>() {
                    @Override
                    public void onComplete(User result) {
                        if(null != result){
                            //将Token令牌设置为程序的全局变量
                            app.setToken(result.getPasswordOrToken());
                            app.setUser(result);
                            //跳转到首页
                            mHandler.sendEmptyMessage(0x123);
                            //将数据添加到数据库
                            mUserDbAPI.saveItem(result);
                        }
                    }
                });
                //延迟发送账号和密码错误的提示
                mHandler.sendEmptyMessageDelayed(0x124, 2000);
                break;
            //忘记密码
            case R.id.tv_forget_password:
//                Toast.makeText(this, "忘记密码", Toast.LENGTH_SHORT).show();
                Intent findPasswordIntent = new Intent(this, FindPasswordActivity.class);
                startActivity(findPasswordIntent);
                break;
            //注册新账号
            case R.id.tv_register:
//                Toast.makeText(this, "注册新账号", Toast.LENGTH_SHORT).show();
                Intent registerIntent = new Intent(this, RegisterActivity.class);
                startActivity(registerIntent);
                break;
        }
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                //结束当前页面
                case 0x123:
                    removeMessages(0x124);
//                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
//                    startActivity(mainIntent);
                    setResult(0);
                    finish();
                    break;
                //弹出账号和密码错误的提示
                case 0x124:
                    Toast.makeText(LoginActivity.this, "账号或密码错误，请重新输入!", Toast.LENGTH_SHORT).show();
                    //将密码设置为空
                    mPasswordEdText.setText("");
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
