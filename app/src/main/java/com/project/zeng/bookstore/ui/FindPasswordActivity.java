package com.project.zeng.bookstore.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.project.zeng.bookstore.entities.User;
import com.project.zeng.bookstore.listeners.DataListener;
import com.project.zeng.bookstore.listeners.TextChangeListener;
import com.project.zeng.bookstore.net.UserAPI;
import com.project.zeng.bookstore.net.impl.UserAPIImpl;

/**
 * Created by zeng on 2017/3/7.
 * 重设密码的Activity
 */

public class FindPasswordActivity extends Activity implements OnClickListener{

    //组件
    private ImageView mBackImgView;

    private EditText mNewPasswordEdText;
    private ImageView mClearNewPasswordView;
    private Button mCommitBtn;

    //操作用户的网络API
    UserAPI mUserAPI = new UserAPIImpl();

    private String id;//账号或手机

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_find_password);
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("title", "重设密码");
        startActivityForResult(intent, 0);//有返回值的Intent
        init();
        initListener();
    }

    /**
     * 初始化组件
     */
    private void init(){
        mBackImgView = (ImageView)findViewById(R.id.iv_find_password_back);
        mNewPasswordEdText = (EditText)findViewById(R.id.et_input_new_password);
        mClearNewPasswordView = (ImageView)findViewById(R.id.iv_clear_new_password);
        mCommitBtn = (Button)findViewById(R.id.btn_commit_password);
    }

    /**
     * 初始化事件
     */
    private void initListener(){
        mBackImgView.setOnClickListener(this);
        mNewPasswordEdText.addTextChangedListener(new TextChangeListener(mClearNewPasswordView, 20));//20:密码最大位数
        mClearNewPasswordView.setOnClickListener(this);
        mCommitBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_commit_password:
                String newPassword = mNewPasswordEdText.getText().toString();
                if(TextUtils.isEmpty(newPassword)){
                    Toast.makeText(this, "请输入新密码!", Toast.LENGTH_SHORT).show();
                    break;
                }else if(newPassword.length() < 6){
                    Toast.makeText(this, "密码位数不得少于6位!", Toast.LENGTH_SHORT).show();
                    break;
                }
                mUserAPI.modifyPassword(new User(id, newPassword), new DataListener<String>() {
                    @Override
                    public void onComplete(String result) {
                        if(result.contains("success")){
                            Toast.makeText(FindPasswordActivity.this, "修改密码成功!", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(FindPasswordActivity.this, "修改密码失败!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.iv_clear_new_password://清除新密码
                mNewPasswordEdText.setText("");
                break;
            case R.id.iv_find_password_back://返回按钮
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            //从RegisterActivity传递回来的id
            case 0:
                switch (resultCode){
                    case 0:
                        id = data.getStringExtra("id");
//                        Log.e("findPasswordActivity", "id=" + id);
                        break;
                    case 1:
                        //RegisterActivity返回按钮
                        finish();
                        break;
                }
                break;
        }
    }
}
