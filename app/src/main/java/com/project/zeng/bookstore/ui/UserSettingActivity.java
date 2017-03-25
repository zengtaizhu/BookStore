package com.project.zeng.bookstore.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.project.zeng.bookstore.MyApplication;
import com.project.zeng.bookstore.db.AbsDBAPI;
import com.project.zeng.bookstore.db.models.DbFactory;
import com.project.zeng.bookstore.entities.Result;
import com.project.zeng.bookstore.entities.User;
import com.project.zeng.bookstore.listeners.DataListener;
import com.project.zeng.bookstore.net.UserAPI;
import com.project.zeng.bookstore.net.impl.UserAPIImpl;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by zeng on 2017/3/23.
 * 用户信息设置的Activity
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class UserSettingActivity extends Activity implements OnClickListener{

    //ViewHolder
    private ViewHolder mViewHolder;
    private User mUser;

    private MyApplication app;

    //常量
    private int SELECT_PIC_BY_TACK_PHOTO = 1;//通过拍照获取图片的请求码
    private int SELECT_PIC_BY_PICK_PHOTO = 2;//通过图库获取图片的请求码
    private String[] majors = null;//专业列表
    private String[] grades = null;//年级列表

    //用户的网络请求API
    UserAPI mUserAPI = new UserAPIImpl();
    //操作用户的数据库API
    AbsDBAPI<User> mUserDbAPI = DbFactory.createUserModel();
    //Handler
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                //更新用户的头像
                case 0x321:
                    Bitmap bmp = (Bitmap) msg.obj;
                    mViewHolder.mUserImgView.setImageBitmap(bmp);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user_setting);
        app = (MyApplication) getApplication();
        mUser = app.getUser();
        //预加载专业和年级信息
        init();
        initListener();
        loadData();
    }

    /**
     * 初始化组件
     */
    private void init(){
        mViewHolder = new ViewHolder(this);
//        Log.e("UserSettingActivity", "pictureUrl=" + mUser.getPictureUrl());
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

    /**
     * 预加载数据
     */
    private void loadData(){
        mUserAPI.fetchMajors(new DataListener<String[]>() {
            @Override
            public void onComplete(String[] result) {
                majors = result;
            }
        });
        mUserAPI.fetchGrades(new DataListener<String[]>() {
            @Override
            public void onComplete(String[] result) {
                grades = result;
            }
        });
        Log.e("UserSettingActivity", "预加载完毕");
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
//                Toast.makeText(this, "修改头像", Toast.LENGTH_SHORT).show();
                showUserImgDialog();
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
//                Toast.makeText(this, "修改年级", Toast.LENGTH_SHORT).show();
                showUserGradeDialog();
                break;
        }
    }

    /**
     * 修改用户头像
     */
    private void showUserImgDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("修改头像");
        String[] items = new String[]{"相册", "拍照"};
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
//                        Toast.makeText(UserSettingActivity.this, "相册", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);
                        break;
                    case 1:
                        Toast.makeText(UserSettingActivity.this, "暂无此功能，敬请期待!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        dialog.show();
    }

    /**
     * 修改名字的对话框
     */
    private void showUserNameDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        final View view = LayoutInflater.from(this).inflate(R.layout.dialog_modify_username, null);
        dialog.setTitle("修改名字");
        dialog.setView(view);
        dialog.setPositiveButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final EditText mNewUsername = (EditText) view.findViewById(R.id.et_new_username);
                final String newUsername = mNewUsername.getText().toString().trim();
                if(!newUsername.equals("")){
                    mUserAPI.modifyUserName(app.getToken(), newUsername, new DataListener<Result>() {
                        @Override
                        public void onComplete(Result result) {
                            if(result.getResult().equals("success")){
                                mViewHolder.mUserNameView.setText(newUsername);
                                mUser.setUsername(newUsername);
                            }else{
                                Toast.makeText(UserSettingActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
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
                final String sex = getResources().getStringArray(R.array.sex)[which];
                mUserAPI.modifyUserSex(app.getToken(), sex, new DataListener<Result>() {
                    @Override
                    public void onComplete(Result result) {
                        if(result.getResult().equals("success")){
                            mViewHolder.mUserSexView.setText(sex);
                            mUser.setSex(sex);
                        }else{
                            Toast.makeText(UserSettingActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        dialog.show();
    }

    /**
     * 修改专业
     */
    private void showUserMajorDialog(){
        if(majors == null){
            return;
        }
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("修改专业");
        dialog.setItems(majors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String major = majors[which];
//                Log.e("UserSettingActivity", "major=" + major);
                mUserAPI.modifyUserMajor(app.getToken(), major, new DataListener<Result>() {
                    @Override
                    public void onComplete(Result result) {
                        if(result.getResult().contains("success")){
                            mViewHolder.mUserMajorView.setText(major);
                            mUser.setMajor(major);
                        }else{
                            Toast.makeText(UserSettingActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        dialog.show();
    }

    /**
     * 修改年级
     */
    private void showUserGradeDialog(){
        if(grades == null){
            return;
        }
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("修改年级");
        dialog.setItems(grades, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String grade = grades[which];
//                Log.e("UserSettingActivity", "grade=" + grade);
                mUserAPI.modifyUserGrade(app.getToken(), grade, new DataListener<Result>() {
                    @Override
                    public void onComplete(Result result) {
                        if(result.getResult().contains("success")){
                            mViewHolder.mUserGradeView.setText(grade);
                            mUser.setGrade(grade);
                        }else{
                            Toast.makeText(UserSettingActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == SELECT_PIC_BY_PICK_PHOTO){//从相册取图片
            if(null == data){
                Toast.makeText(this, "选择图片出错!", Toast.LENGTH_SHORT).show();//没有选择图片或者选择出错
                return;
            }
            Uri uri = data.getData();
            Picasso.with(this).load(uri).resize(360, 270).into(new Target() {
                @Override
                public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                    mUserAPI.modifyUserImg(app.getToken(), bitmap, new DataListener<Result>() {
                        @Override
                        public void onComplete(Result result) {
                            if(result.getResult().contains("success")){
                                mUser.setPictureUrl(result.getMessage());
                                Log.e("UserSettingActivity", mUser.getPictureUrl());
                                //更新用户头像
                                Message msg = new Message();
                                msg.what = 0x321;
                                msg.obj = bitmap;
                                mHandler.sendMessage(msg);
                            }else{
                                Toast.makeText(UserSettingActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();//没有选择图片或者选择出错
                            }
                        }
                    });
                }

                @Override
                public void onBitmapFailed(Drawable drawable) {
                    Toast.makeText(UserSettingActivity.this, "头像加载失败!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onPrepareLoad(Drawable drawable) {

                }
            });
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mUserDbAPI.saveItem(mUser);//结束该Activity前，将用户存入数据库
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
