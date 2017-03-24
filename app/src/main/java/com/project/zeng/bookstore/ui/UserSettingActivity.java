package com.project.zeng.bookstore.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.de.hdodenhof.circleimageview.CircleImageView;
import com.example.zeng.bookstore.R;
import com.project.zeng.bookstore.entities.User;
import com.project.zeng.bookstore.net.UserAPI;
import com.project.zeng.bookstore.net.impl.UserAPIImpl;
import com.squareup.picasso.Picasso;

/**
 * Created by zeng on 2017/3/23.
 * 用户信息设置的Activity
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class UserSettingActivity extends Activity implements OnClickListener{

    //ViewHolder
    private ViewHolder mViewHolder;
    private User mUser;
    //用户的网络请求
    UserAPI mUserAPI = new UserAPIImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user_setting);
        init();
        initListener();
    }

    /**
     * 初始化组件
     */
    private void init(){
        mViewHolder = new ViewHolder(this);
        mUser = (User) getIntent().getExtras().getSerializable("user");
        Log.e("UserSettingActivity", "pictureUrl=" + mUser.getPictureUrl());
        Picasso.with(this).load(mUser.getPictureUrl()).fit().into(mViewHolder.mUserImgView);
        mViewHolder.mUserNameView.setText(mUser.getUsername());
        mViewHolder.mUserSexView.setText(mUser.getSex());
        mViewHolder.mUserGradeView.setText(mUser.getGrade());
        mViewHolder.mUserMajorView.setText(mUser.getMajor());
    }

    /**
     * 初始化事件及适配器
     */
    private void initListener(){
        mViewHolder.mBackView.setOnClickListener(this);
        mViewHolder.mUserImgLayout.setOnClickListener(this);
        mViewHolder.mUserNameLayout.setOnClickListener(this);
        mViewHolder.mUserSexLayout.setOnClickListener(this);
        mViewHolder.mUserMajorLayout.setOnClickListener(this);
        mViewHolder.mUserGradeLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //返回按钮
            case R.id.iv_user_back:
                finish();
                break;
            //修改头像
            case R.id.rl_user_img:
                Toast.makeText(this, "修改头像", Toast.LENGTH_SHORT).show();
                break;
            //修改用户名
            case R.id.rl_username:
//                Toast.makeText(this, "修改用户名", Toast.LENGTH_SHORT).show();
                showUserNameDialog();
                break;
            //修改性别
            case R.id.rl_user_sex:
//                Toast.makeText(this, "修改性别", Toast.LENGTH_SHORT).show();
                showUserSexDialog();
                break;
            //修改专业
            case R.id.rl_user_major:
//                Toast.makeText(this, "修改专业", Toast.LENGTH_SHORT).show();
                showUserMajorDialog();
                break;
            //修改年级
            case R.id.rl_user_grade:
                Toast.makeText(this, "修改年级", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 修改名字的对话框
     */
    private void showUserNameDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        final View view = LayoutInflater.from(this).inflate(R.layout.dialog_modify_username, null);
        dialog.setView(view);
        dialog.setPositiveButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final EditText mNewUsername = (EditText) view.findViewById(R.id.et_new_username);
                String newUsername = mNewUsername.getText().toString().trim();
                if(!newUsername.equals("")){
                    mViewHolder.mUserNameView.setText(newUsername);
//                    Log.e("UserSettingActivity", "username=" + newUsername);
                }
            }
        });
        dialog.show();
    }

    /**
     * 修改性别的对话框
     */
    private void showUserSexDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("修改性别");
        dialog.setItems(R.array.sex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String sex = getResources().getStringArray(R.array.sex)[which];
                mViewHolder.mUserSexView.setText(sex);
            }
        });
        dialog.show();
    }

    /**
     * 修改专业
     */
    private void showUserMajorDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("修改性别");
        dialog.setItems(R.array.sex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String sex = getResources().getStringArray(R.array.sex)[which];
                mViewHolder.mUserSexView.setText(sex);
            }
        });
        dialog.show();
    }

    /**
     * 此Activity的ViewHolder
     */
    private static class ViewHolder{
        private ImageView mBackView;
        private RelativeLayout mUserImgLayout;
        private CircleImageView mUserImgView;
        private RelativeLayout mUserNameLayout;
        private TextView mUserNameView;
        private RelativeLayout mUserSexLayout;
        private TextView mUserSexView;
        private RelativeLayout mUserMajorLayout;
        private TextView mUserMajorView;
        private RelativeLayout mUserGradeLayout;
        private TextView mUserGradeView;

        public ViewHolder(Activity activity){
            mBackView = (ImageView) activity.findViewById(R.id.iv_user_back);
            mUserImgLayout = (RelativeLayout) activity.findViewById(R.id.rl_user_img);
            mUserImgView = (CircleImageView) activity.findViewById(R.id.civ_user_img);
            mUserNameLayout = (RelativeLayout) activity.findViewById(R.id.rl_username);
            mUserNameView = (TextView) activity.findViewById(R.id.tv_username);
            mUserSexLayout = (RelativeLayout) activity.findViewById(R.id.rl_user_sex);
            mUserSexView = (TextView) activity.findViewById(R.id.tv_user_sex);
            mUserMajorLayout = (RelativeLayout) activity.findViewById(R.id.rl_user_major);
            mUserMajorView = (TextView) activity.findViewById(R.id.tv_user_major);
            mUserGradeLayout = (RelativeLayout) activity.findViewById(R.id.rl_user_grade);
            mUserGradeView = (TextView) activity.findViewById(R.id.tv_user_grade);
        }
    }
}
