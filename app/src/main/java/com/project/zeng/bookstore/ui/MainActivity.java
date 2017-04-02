package com.project.zeng.bookstore.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.example.zeng.bookstore.R;
import com.project.zeng.bookstore.MyApplication;
import com.project.zeng.bookstore.entities.User;
import com.project.zeng.bookstore.listeners.DataListener;
import com.project.zeng.bookstore.net.UserAPI;
import com.project.zeng.bookstore.net.impl.UserAPIImpl;
import com.project.zeng.bookstore.ui.frgm.CartFragment;
import com.project.zeng.bookstore.ui.frgm.CategoryFragment;
import com.project.zeng.bookstore.ui.frgm.HomeFragment;
import com.project.zeng.bookstore.ui.frgm.HomeFragment.OnToolbarClickListener;
import com.project.zeng.bookstore.ui.frgm.MeFragment;

public class MainActivity extends BaseActivity implements OnCheckedChangeListener, OnToolbarClickListener {

    //Application，保存全局变量
    private MyApplication app;

    //组件
    private RadioGroup mRdoGroupMenu;

    //Fragment
    private HomeFragment mHomeFragment;//首页
    private CategoryFragment mCategoryFragment;//分类
    private CartFragment mCartFragment;//购物车
    private MeFragment mMeFragment;//我的

    public int[] mUpImageIds = new int[]{R.mipmap.menu_home_up, R.mipmap.menu_category_up,
            R.mipmap.menu_cart_up, R.mipmap.menu_me_up};
    public int[] mDownImageIds = new int[]{R.mipmap.menu_home_down, R.mipmap.menu_category_down,
            R.mipmap.menu_cart_down, R.mipmap.menu_me_down};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        app = (MyApplication)getApplication();
//        testCase();
        //初始化Fragment
        initFragment();
        init();
    }

    /**
     * 用于测试------------------------替代登录获取token的步骤，实验完即删除
     */
    private void testCase(){
        UserAPI userAPI = new UserAPIImpl();
        userAPI.fetchUserById(new User("201330350225", "123"), new DataListener<User>() {
            @Override
            public void onComplete(User result) {
                if(result != null){
                    app.setToken(result.getPasswordOrToken());
//                    Log.e("MainActivity", "token = " + app.getToken());
                    app.setUser(result);//----------------在登录后缓存，即此处
                }
            }
        });
    }

    /**
     * 初始化组件、数据以及界面
     */
    private void init(){
        //初始化组件及其事件
        mRdoGroupMenu = (RadioGroup)findViewById(R.id.rdoGroup_main_menu);
        mRdoGroupMenu.setOnCheckedChangeListener(this);
        //设置Fragment Container
        setFragmentContainer(R.id.fl_main_container);
        //初始化界面，默认加载首页
        mHomeFragment.setRetainInstance(true);
        mHomeFragment.fetchData();
        //设置首页图标为选中按下状态
        mRdoGroupMenu.getChildAt(0).setBackgroundResource(R.mipmap.menu_home_down);
        addFragment(mHomeFragment);
    }

    /**
     * 初始化Fragment
     */
    private void initFragment(){
        mHomeFragment = new HomeFragment();
        mCategoryFragment = new CategoryFragment();
        mCartFragment = new CartFragment();
        mMeFragment = new MeFragment();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        //初始化RadioGroup的图标
        initRadioButtonImg();
        if(!app.getReceiver().isAvailable){
            Toast.makeText(this, "您的网络在偷懒，请重试", Toast.LENGTH_SHORT).show();
        }else if(app.getToken().equals("")){
            Toast.makeText(this, "请先登录，不然买不了东西哦!", Toast.LENGTH_SHORT).show();
        }
        switch (checkedId){
            case R.id.rdoBtn_main_home:{
                mHomeFragment.fetchData();
                replaceFragment(mHomeFragment);
                setRadioButtonImg(0);
                break;
            }
            case R.id.rdoBtn_main_category:{
                mCategoryFragment.fetchCategories();
                replaceFragment(mCategoryFragment);
                setRadioButtonImg(1);
                break;
            }
            case R.id.rdoBtn_main_cart:{
                if(mCartFragment == null){
                    mCartFragment = new CartFragment();
                }
                replaceFragment(mCartFragment);
                mCartFragment.fetchData(app.getToken());
                setRadioButtonImg(2);
                break;
            }
            case R.id.rdoBtn_main_mystore:{
                if(mMeFragment == null){
                    mMeFragment = new MeFragment();
                }
                replaceFragment(mMeFragment);
                setRadioButtonImg(3);
                break;
            }
            default:
                break;
        }
    }

    /**
     * 初始化RadioButton的图标
     */
    private void initRadioButtonImg(){
        for(int i = 0; i < mRdoGroupMenu.getChildCount(); i++){
            mRdoGroupMenu.getChildAt(i).setBackgroundResource(mUpImageIds[i]);
        }
    }

    /**
     * 设置RadioButton的图标
     * @param imageId
     */
    private void setRadioButtonImg(int imageId){
        mRdoGroupMenu.getChildAt(imageId).setBackgroundResource(mDownImageIds[imageId]);
    }

    /**
     * HomeFragment的Toolbar的点击事件的响应
     * @param flag
     */
    @Override
    public void onChangeAction(int flag) {
        switch (flag){
            case 0:
                //模拟点击“分类”RadioButton
                mRdoGroupMenu.getChildAt(1).performClick();
//                Log.e("MainActivity", "接收消息");
                break;
            case 1:
                //进入商品搜索Activity
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        app.startGetTokenService();//开始获取token的Service
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        app.stopGetTokenService();//停止获取token的Service
        super.onDestroy();
    }
}
