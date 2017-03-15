package com.project.zeng.bookstore.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.example.zeng.bookstore.R;
import com.project.zeng.bookstore.MyApplication;
import com.project.zeng.bookstore.ui.frgm.CartActivity;
import com.project.zeng.bookstore.ui.frgm.CartFragment;
import com.project.zeng.bookstore.ui.frgm.CategoryFragment;
import com.project.zeng.bookstore.ui.frgm.FindFragment;
import com.project.zeng.bookstore.ui.frgm.HomeFragment;
import com.project.zeng.bookstore.ui.frgm.HomeFragment.OnToolbarClickListener;
import com.project.zeng.bookstore.ui.frgm.MeFragment;

public class MainActivity extends BaseActivity implements OnCheckedChangeListener, OnToolbarClickListener {

    //Application，保存全局变量
    private MyApplication app;

    //组件
    private RadioGroup mRdoGroupMenu;

    //Fragment
    HomeFragment mHomeFragment;//首页
    CategoryFragment mCategoryFragment;//分类
    FindFragment mFindFragment;//发现
    CartFragment mCartFragment;//购物车
    MeFragment mMeFragment;//我的

    public int[] mUpImageIds = new int[]{R.mipmap.menu_home_up, R.mipmap.menu_category_up,
            R.mipmap.menu_find_up, R.mipmap.menu_cart_up, R.mipmap.menu_me_up};
    public int[] mDownImageIds = new int[]{R.mipmap.menu_home_down, R.mipmap.menu_category_down,
            R.mipmap.menu_find_down, R.mipmap.menu_cart_down, R.mipmap.menu_me_down};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化Fragment
        initFragment();
        init();
        app = (MyApplication)getApplication();
//        Toast.makeText(this, "token=" + app.getToken(), Toast.LENGTH_SHORT).show();
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
        mFindFragment = new FindFragment();
        mCartFragment = new CartFragment();
        mMeFragment = new MeFragment();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        //初始化RadioGroup的图标
        initRadioButtonImg();
        switch (checkedId){
            case R.id.rdoBtn_main_home:{
                mHomeFragment.fetchData();
                replaceFragment(mHomeFragment);
                setRadioButtonImg(0);
                break;
            }
            case R.id.rdoBtn_main_category:{
                mCategoryFragment.fetchRecommends();
                replaceFragment(mCategoryFragment);
                setRadioButtonImg(1);
                break;
            }
            case R.id.rdoBtn_main_find:{
                if(mFindFragment == null){
                    mFindFragment = new FindFragment();
                }
                replaceFragment(mFindFragment);
                setRadioButtonImg(2);
                break;
            }
            case R.id.rdoBtn_main_cart:{
                if(mCartFragment == null){
                    mCartFragment = new CartFragment();
                }
                replaceFragment(mCartFragment);
                setRadioButtonImg(3);
                break;
            }
            case R.id.rdoBtn_main_mystore:{
                if(mMeFragment == null){
                    mMeFragment = new MeFragment();
                }
                replaceFragment(mMeFragment);
                setRadioButtonImg(4);
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
                Log.e("MainActivity", "接收消息");
                break;
            case 1:
                //进入商品搜索Activity
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
        }
    }
}
