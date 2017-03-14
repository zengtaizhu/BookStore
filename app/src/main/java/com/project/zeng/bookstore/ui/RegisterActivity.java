package com.project.zeng.bookstore.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
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
import com.project.zeng.bookstore.entities.User;
import com.project.zeng.bookstore.listeners.DataListener;
import com.project.zeng.bookstore.listeners.TextChangeListener;
import com.project.zeng.bookstore.net.UserAPI;
import com.project.zeng.bookstore.net.impl.UserAPIImpl;

import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by zeng on 2017/3/7.
 */

public class RegisterActivity extends Activity implements OnClickListener{

    //组件
    private TextView mTitleTxtView;

    private ImageView mBackImgView;

    private EditText mPhoneEdtTxt;
    private ImageView mClearPhoneImgView;
    private Button mGetVerifyCodeBtn;

    private EditText mVerifyCodeEdtTxt;
    private ImageView mClearVerifyCodeImgView;
    private Button mSubmitVerifyCodeBtn;

    private ProgressDialog mWatingDialog;

    private String mPhone;//本机电话

    //操作用户的网络API
    UserAPI mUserAPI = new UserAPIImpl();

    private MyApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        init();
        initListener();
        //若是忘记密码的界面跳转到注册界面，则设置布局的标题
        if(getIntent().hasExtra("title")){
            mTitleTxtView.setText(getIntent().getStringExtra("title"));
        }
        //注册短信回调监听
        SMSSDK.registerEventHandler(eventHandler);
        app = (MyApplication) getApplication();
    }

    /**
     * 初始化组件
     */
    private void init(){
        mTitleTxtView = (TextView)findViewById(R.id.tv_register_title);
        mBackImgView = (ImageView)findViewById(R.id.iv_register_back);
        mPhoneEdtTxt = (EditText)findViewById(R.id.et_input_phone);
        mClearPhoneImgView = (ImageView)findViewById(R.id.iv_clear_phone);
        mGetVerifyCodeBtn = (Button)findViewById(R.id.btn_get_verify_code);
        mVerifyCodeEdtTxt = (EditText)findViewById(R.id.et_input_verify_code);
        mClearVerifyCodeImgView = (ImageView)findViewById(R.id.iv_clear_verify_code);
        mSubmitVerifyCodeBtn = (Button)findViewById(R.id.btn_submit_verify_code);
    }

    /**
     * 初始化事件
     */
    private void initListener(){
        mBackImgView.setOnClickListener(this);                               /** 待修改回来----------*/
        mPhoneEdtTxt.addTextChangedListener(new TextChangeListener(mClearPhoneImgView, 12));//11:手机位数
        mClearPhoneImgView.setOnClickListener(this);
        mGetVerifyCodeBtn.setOnClickListener(this);
        mVerifyCodeEdtTxt.addTextChangedListener(new TextChangeListener(mClearVerifyCodeImgView, 4));//4:验证码位数
        mClearVerifyCodeImgView.setOnClickListener(this);
        mSubmitVerifyCodeBtn.setOnClickListener(this);
    }

    /**
     * 短信验证的回调监听函数
     */
    private EventHandler eventHandler = new EventHandler(){

        @Override
        public void afterEvent(int i, int i1, Object o) {
            //判断结果是否完成，验证成功则返回o：HashMap<number,code>
            if(i1 == SMSSDK.RESULT_COMPLETE){
                //回调成功
                if(i == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
                    //提交验证码成功
//                    Log.e("RegisterActivity", "验证码提交成功" + o.toString());
                    HashMap<String, Object> mData = (HashMap<String, Object>) o;
                    //返回的国家编码
                    String country = (String)mData.get("country");
                    //返回的用户注册的手机号码
                    String phone = (String)mData.get("phone");
                    Log.e("RegisterActivity", "country:" + country + "，" + "phone:" + phone);

                    if(phone.equals(mPhone)){
                        mHandler.sendEmptyMessage(0x102);
                    }else{
                        mHandler.sendEmptyMessage(0x103);
                    }
                }else if(i == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                    //获取验证码成功
                    Log.e("RegisterActivity", "获取验证码成功");
                }else if(i == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
                    //返回支持发送验证码的国家列表
//                    Log.e("RegisterActivity", "获得支持验证码的国家列表");
                }
            }else{
                ((Throwable)o).printStackTrace();
            }
        }
    };

    /**
     * 显示一个对话框
     * @param text
     */
    private void showDialog(String text){
        new AlertDialog.Builder(this)
                .setTitle(text)
                .setPositiveButton("确定", null)
                .show();
    }

    /**
     * 显示等待对话框
     */
    private void showProgressDialog(){
        mWatingDialog = new ProgressDialog(this);
        mWatingDialog.setTitle("正在验证，请稍等...");
        mWatingDialog.setCancelable(true);
        mWatingDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //获取验证码
            case R.id.btn_get_verify_code:
                getVerifyCode();
                break;
            //提交验证码
            case R.id.btn_submit_verify_code:
                submitVerifyCode();
                //用于实验绕过验证码的步骤
//                final String id = mPhoneEdtTxt.getText().toString().trim();
//                if(mTitleTxtView.getText().toString().equals("重设密码")){//重设密码
//                    mUserAPI.fetchUserById(new User(id, ""), new DataListener<User>() {
//                        @Override
//                        public void onComplete(User result) {
//                            Log.e("RegisterActivity", "result=" + result);
//                            if(null != result){
//                                app.setToken(result.getPasswordOrToken());
//                            }
//                        }
//                    });
//                    Log.e("RegisterActivity", "id=" + id);
//                    Intent intent = RegisterActivity.this.getIntent();
//                    intent.putExtra("id", id);
//                    RegisterActivity.this.setResult(0, intent);
//                }else{//注册
//                    Intent intent = new Intent(RegisterActivity.this, RegisterPswActivity.class);
//                    intent.putExtra("id", id);
//                    startActivity(intent);
//                }
//                finish();
                break;
            //清除手机号
            case R.id.iv_clear_phone:
                mPhoneEdtTxt.setText("");
                break;
            //清除验证码
            case R.id.iv_clear_verify_code:
                mVerifyCodeEdtTxt.setText("");
                break;
            //返回
            case R.id.iv_register_back:
                Intent intent = getIntent();
                this.setResult(1, intent);
                finish();
                break;
        }
    }

    /**
     * 获取验证码
     */
    public void getVerifyCode(){
        mPhone = mPhoneEdtTxt.getText().toString().trim();
        //发送短信，传入国家号和电话
        if(TextUtils.isEmpty(mPhone)){
            //若没有输入电话
            Toast.makeText(getApplicationContext(), "号码不能为空！！！", Toast.LENGTH_SHORT).show();
        }else{
            mUserAPI.fetchUserById(mPhone, new DataListener<User>() {
                @Override
                public void onComplete(User result) {
                    if(null != result){
                        //显示该账号已被注册的Toast
                        mHandler.sendEmptyMessage(0x100);
                    }
                }
            });
            //延迟获取验证码的请求，即先验证该号码是否已注册
            mHandler.sendEmptyMessageDelayed(0x101, 1000);
        }
    }

    /**
     * 向服务器发送验证码，在监听回调中判断是否验证通过
     */
    public void submitVerifyCode(){
        String verificationCode = mVerifyCodeEdtTxt.getText().toString().trim();
        if(TextUtils.isEmpty(verificationCode)){
            //如果没有输入验证码
            Toast.makeText(getApplicationContext(), "验证码不能为空！", Toast.LENGTH_SHORT).show();
        }else{
            showProgressDialog();
            //提交短信验证码
            SMSSDK.submitVerificationCode("+86", mPhone, verificationCode);
            Toast.makeText(getApplicationContext(), "提交了注册信息：" + mPhone, Toast.LENGTH_SHORT).show();
        }
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                //显示已经被注册的Toast
                case 0x100:
                    removeMessages(0x101);
                    Toast.makeText(getApplicationContext(),  mPhone + "已被注册!", Toast.LENGTH_SHORT).show();
                    break;
                //发送获取验证码的请求
                case 0x101:
                    SMSSDK.getVerificationCode("+86", mPhone);
                    Toast.makeText(getApplicationContext(), "发送成功：" + mPhone, Toast.LENGTH_SHORT).show();
                    break;
                case 0x102:
                    Toast.makeText(RegisterActivity.this, "验证成功！", Toast.LENGTH_SHORT).show();
                    mWatingDialog.dismiss();
                    if(mTitleTxtView.getText().toString().equals("重设密码")){
                        //修改密码：跳转到修改密码的界面
                        mUserAPI.fetchUserById(new User(mPhone, ""), new DataListener<User>() {
                            @Override
                            public void onComplete(User result) {
//                                Log.e("RegisterActivity", "result=" + result);
                                if(null != result){
                                    app.setToken(result.getPasswordOrToken());
                                }
                            }
                        });
//                        Log.e("RegisterActivity", "id=" + mPhone);
                        Intent intent = RegisterActivity.this.getIntent();
                        intent.putExtra("id", mPhone);
                        RegisterActivity.this.setResult(0, intent);
                    }else{
                        //注册：跳转到输入密码的界面
                        Intent intent = new Intent(RegisterActivity.this, RegisterPswActivity.class);
                        intent.putExtra("id", mPhone);
                        startActivity(intent);
                    }
                    finish();
                    break;
                case 0x103:
                    showDialog("验证失败！");
                    mWatingDialog.dismiss();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在Activity销毁时反注册SMSSDK，防止内存泄露
        SMSSDK.unregisterAllEventHandler();
    }

    /**
     * 重写返回按钮，防止按返回键时，出现卡顿
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            mBackImgView.performClick();
        }
        return super.onKeyDown(keyCode, event);
    }
}
