package com.project.zeng.bookstore.ui.frgm;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.zeng.bookstore.R;
import com.project.zeng.bookstore.adapter.HomeCategoryAdapter;
import com.project.zeng.bookstore.adapter.HomeRecommendAdapter;
import com.project.zeng.bookstore.db.AbsDBAPI;
import com.project.zeng.bookstore.db.models.DbFactory;
import com.project.zeng.bookstore.entities.Category;
import com.project.zeng.bookstore.entities.Recommend;
import com.project.zeng.bookstore.listeners.DataListener;
import com.project.zeng.bookstore.net.CategoryAPI;
import com.project.zeng.bookstore.net.RecommendAPI;
import com.project.zeng.bookstore.net.impl.CategoryAPIImpl;
import com.project.zeng.bookstore.net.impl.RecommendAPIImpl;
import com.project.zeng.bookstore.ui.ProductsActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zeng on 2017/2/28.
 * 首页的Fragment
 */

public class HomeFragment extends Fragment implements OnPageChangeListener,
        OnClickListener, OnItemClickListener{

    //组件
    private ViewPager mViewPager;
    private GridView mGridView;
    private ImageView mImageView;
    private EditText mEditText;
    //适配器
    private HomeRecommendAdapter mRecmAdapter;
    private HomeCategoryAdapter mCategoryAdapter;

    //商品推荐网络请求API
    RecommendAPI mRecommendAPI = new RecommendAPIImpl();
    //商品类型网络请求API
    CategoryAPI mCategoryAPI = new CategoryAPIImpl();

    //操作商品推荐的数据库对象
    AbsDBAPI<Recommend> mRecmDbAPI = DbFactory.createRecommendModel();
    //操作商品类型数据的数据库对象
    AbsDBAPI<Category> mCatgDbAPI = DbFactory.createCategoryModel();

    private static int currentItem;//ViewPager当前的页数

    private List<Category> mCategories;
    private List<Recommend> mRecommends;

    private ImageHandler mImageHandler = new ImageHandler(new WeakReference<>(this));

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        init(view);
        initListener();
        return view;
    }

    /**
     * 初始化组件
     * @param view
     */
    private void init(View view){
        mViewPager = (ViewPager) view.findViewById(R.id.vp_home_recommend);
        mGridView = (GridView) view.findViewById(R.id.gd_home_category);
        mImageView = (ImageView) view.findViewById(R.id.iv_home_toolbar_to_category);
        mEditText = (EditText) view.findViewById(R.id.et_pro_toolbar_search);
    }

    /**
     * 初始化事件以及适配器
     */
    private void initListener(){
        mCategories = new ArrayList<>();
        mRecommends = new ArrayList<>();
        mRecmAdapter = new HomeRecommendAdapter(getActivity().getApplication());
        mViewPager.setAdapter(mRecmAdapter);
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setCurrentItem(Integer.MAX_VALUE / 2);//默认在中间，使用户看不到边界
        mCategoryAdapter = new HomeCategoryAdapter(getActivity().getApplication());
        mGridView.setAdapter(mCategoryAdapter);
        mGridView.setOnItemClickListener(this);
        mImageView.setOnClickListener(this);
        mEditText.setOnClickListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        //重新设置ViewPager的当前页数
        mImageHandler.sendMessage(Message.obtain(mImageHandler, ImageHandler.MSG_PAGE_CHANGED, position, 0));
        if(mRecommends.size() != 0){
            currentItem = position % mRecommends.size();
        }
//        Log.e("viewpager", "点击的位置=" + currentItem);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //实现轮番播放的效果
        switch (state){
            case ViewPager.SCROLL_STATE_DRAGGING://当手指在ViewPager滑动时
                mImageHandler.sendEmptyMessage(ImageHandler.MSG_KEEP_SILENT);
//                Log.e("HomeFragment", "ViewPager.SCROLL_STATE_DRAGGING");
                break;
            case ViewPager.SCROLL_STATE_IDLE://当手指离开ViewPager时
                mImageHandler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);
//                Log.e("HomeFragment", "ViewPager.SCROLL_STATE_IDLE");
                break;
            case ViewPager.SCROLL_STATE_SETTLING://
//                Log.e("HomeFragment", "ViewPager.SCROLL_STATE_SETTLING");
                break;
            default:
                break;
        }
    }

    /**
     * 获取数据后自动播放
     */
    public void fetchData(){
        mRecmDbAPI.loadDatasFromDb(new DataListener<List<Recommend>>() {
            @Override
            public void onComplete(List<Recommend> result) {
//                Log.e("HomeFragment","尝试从数据库加载Recommend=" + result.size());
                mRecommendAPI.fetchRecommends(new DataListener<List<Recommend>>() {
                    @Override
                    public void onComplete(List<Recommend> result) {
//                        Log.e("HomeFragment", "获取的Recommend数量为：" + result.size());
                        if(null != result){
                            mRecommends = result;
                        }
                        mRecmAdapter.updateData(result);
                        //开始自动播放
                        mImageHandler.sendEmptyMessage(ImageHandler.MSG_UPDATE_IMAGE);
                        //先清除旧数据，然后将新数据存储到数据库中
                        mRecmDbAPI.deleteAll();
                        mRecmDbAPI.saveItems(result);
                    }
                });
            }
        });
        mCatgDbAPI.loadDatasFromDb(new DataListener<List<Category>>() {
            @Override
            public void onComplete(List<Category> result) {
//                Log.e("HomeFragment", "尝试从数据库加载Category");
                mCategoryAPI.fetchRecommends(new DataListener<List<Category>>() {
                    @Override
                    public void onComplete(List<Category> result) {
//                        Log.e("HeadFragment", "获得的Category数量为：" + result.size());
                        if(null != result){
                            mCategories = result;
                        }
                        mCategoryAdapter.updateData(result);
                        //将数据存储到数据库中
                        mCatgDbAPI.saveItems(result);
                    }
                });
            }
        });
    }

    private OnToolbarClickListener mClickListener;

    private Context mMainContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainContext = context;
        try{
            mClickListener = (OnToolbarClickListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement OnClickListener");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_home_toolbar_to_category:
                //向MainActivity传递消息，通知MainActivity切换Fragment
                mClickListener.onChangeAction(0);
//                Log.e("HomeFragment", "传递消息");
                break;
            case R.id.et_pro_toolbar_search:
//                Log.e("HomeFragment", "点击了");
                mClickListener.onChangeAction(1);
                break;
            case R.id.vp_home_recommend:
                Log.e("HomeFragment", "点击了一个");
                Intent intent = new Intent(mMainContext, ProductsActivity.class);
                intent.putExtra("key", mRecommends.get(currentItem).getProId());
                intent.putExtra("name", mRecommends.get(currentItem).getProName());
                //商品搜索类型：1--代表通过关键字查找商品
                intent.putExtra("type", "1");
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String categoryId = mCategories.get(position).getId();
        String categoryName = mCategories.get(position).getName();
        Intent intent = new Intent(mMainContext, ProductsActivity.class);
        intent.putExtra("key", categoryId);
        intent.putExtra("name", categoryName);
        intent.putExtra("type", "2");
        startActivity(intent);
    }


    /**
     * 回调接口
     */
    public interface OnToolbarClickListener{
        public void onChangeAction(int flag);
    }

    /**
     * 图片加载的Handler
     */
    private static class ImageHandler extends Handler{

        //请求更新显示的View
        protected static final int MSG_UPDATE_IMAGE  = 1;
        //请求暂停轮播
        protected static final int MSG_KEEP_SILENT   = 2;
        //请求恢复轮播
        protected static final int MSG_BREAK_SILENT  = 3;
        //记录最新的页号
        protected static final int MSG_PAGE_CHANGED  = 4;
        //轮播间隔时间
        protected static final long MSG_DELAY = 3000;

        //使用弱引用避免Handler泄露
        private WeakReference<HomeFragment> weakReference;
        //当前的页号
        private int currentItem = 0;

        public ImageHandler(WeakReference<HomeFragment> reference){
            this.weakReference = reference;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            Log.e("homeFrgm_handler", "receive_msg:" + msg.what);
            HomeFragment frgm = weakReference.get();
            if(frgm == null){
                //若HomeFragment被回收，则退出处理
                return;
            }
            //检查消息队列并移除为发送的消息
            if(frgm.mImageHandler.hasMessages(MSG_UPDATE_IMAGE)){
                frgm.mImageHandler.removeMessages(MSG_UPDATE_IMAGE);
            }
            switch (msg.what){
                case MSG_UPDATE_IMAGE:{//更新下一张
                    currentItem++;
                    frgm.mViewPager.setCurrentItem(currentItem);
                    //准备下次播放
                    frgm.mImageHandler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
                }
                case MSG_KEEP_SILENT:{//停止更新，即保持现状
                    break;
                }
                case MSG_BREAK_SILENT:{//当继续滑动时，更新下一张
                    frgm.mImageHandler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
                }
                case MSG_PAGE_CHANGED:{
                    //记录当前的页号
                    currentItem = msg.arg1;
                    break;
                }
                default:
                    break;
            }
        }
    }
}
