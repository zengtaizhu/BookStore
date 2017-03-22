package com.project.zeng.bookstore.adapter;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zeng.bookstore.R;
import com.project.zeng.bookstore.ui.frgm.MeFragment;
import com.project.zeng.bookstore.ui.frgm.OrderFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zeng on 2017/3/21.
 * MeFragment的ViewPager的切换Adapter
 */

public class OrderPagerAdapter extends PagerAdapter implements OnPageChangeListener{

    private MeFragment mParentFrgm;//引用MeFragment

    private ViewHolder mViewHolder;//订单ViewPager的ViewHolder
    private List<OrderFragment> mFragments;
    private FragmentManager mFrgmManager;
    private int mScreenOneOfFour = 0;
    private int currentPageIndex = 0;//当前的页面索引

    public OrderPagerAdapter(MeFragment fragment, View view){
        this.mParentFrgm = fragment;
        mViewHolder = new ViewHolder(view);//初始化组件
        this.mFrgmManager = this.mParentFrgm.getFragmentManager();
        init();
    }

    /**
     * 初始化Adapter
     */
    private void init(){
        initFragments();//初始化Fragment数组
        mViewHolder.mOrderPager.addOnPageChangeListener(this);//给ViewPager添加事件
        mScreenOneOfFour = mParentFrgm.getScreenWidth() / 4;
        initTabLine();
    }

    /**
     * 初始化Fragment数组，即四个ViewPager的页面
     */
    private void initFragments(){
        mFragments = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            mFragments.add(new OrderFragment());
        }
    }

    /**
     * 初始化TabLine，使其宽度为屏幕的四分之一
     */
    private void initTabLine(){
        ViewGroup.LayoutParams layoutParams = mViewHolder.mTabLineView.getLayoutParams();
        layoutParams.width = mScreenOneOfFour;
        mViewHolder.mTabLineView.setLayoutParams(layoutParams);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mFragments.get(position).getView());//移除ViewPager两边的Page布局
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
//        Log.e("OrderPagerAdapter", "position=" + position);
        OrderFragment fragment = mFragments.get(position);
        if(!fragment.isAdded()){//若未添加，则将Fragment添加到ViewPager中
            FragmentTransaction ft = mFrgmManager.beginTransaction();
            fragment.fetchData(position);
            ft.add(fragment, fragment.getClass().getSimpleName());
            ft.commit();
            mFrgmManager.executePendingTransactions();//立即执行添加操作
        }
        if(null == fragment.getView().getParent()){
            container.addView(fragment.getView());//为ViewPager添加布局
        }
        return fragment.getView();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mViewHolder.mTabLineView.getLayoutParams();
        lp.leftMargin = (int)(currentPageIndex * mScreenOneOfFour + (positionOffset + position - currentPageIndex) * mScreenOneOfFour);
        mViewHolder.mTabLineView.setLayoutParams(lp);//设置TabLine的右偏移量
    }

    @Override
    public void onPageSelected(int position) {
        //将ViewPager上的订单状态的TextView设置为默认颜色
        resetTextView();
        //将选中的页码对于的TextView设置为红色
        switch (position){
            //全部订单
            case 0:
                mViewHolder.mAllOrderView.setTextColor(mParentFrgm.getResources().getColor(R.color.text_red));
                break;
            //待发货订单
            case 1:
                mViewHolder.mDeliverView.setTextColor(mParentFrgm.getResources().getColor(R.color.text_red));
                break;
            //待评价订单
            case 2:
                mViewHolder.mCommentView.setTextColor(mParentFrgm.getResources().getColor(R.color.text_red));
                break;
            //退款订单
            case 3:
                mViewHolder.mReturnView.setTextColor(mParentFrgm.getResources().getColor(R.color.text_red));
                break;
            default:
                break;
        }
        currentPageIndex = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 初始化ViewPager的TabLine
     */
    private void resetTextView(){
        mViewHolder.mAllOrderView.setTextColor(mParentFrgm.getResources().getColor(R.color.text_black));
        mViewHolder.mDeliverView.setTextColor(mParentFrgm.getResources().getColor(R.color.text_black));
        mViewHolder.mCommentView.setTextColor(mParentFrgm.getResources().getColor(R.color.text_black));
        mViewHolder.mReturnView.setTextColor(mParentFrgm.getResources().getColor(R.color.text_black));
    }

    /**
     * MeFragment的订单ViewPager的ViewHolder
     */
    private static class ViewHolder{

        private TextView mAllOrderView;
        private TextView mDeliverView;
        private TextView mCommentView;
        private TextView mReturnView;
        private ImageView mTabLineView;
        private ViewPager mOrderPager;

        public ViewHolder(View view) {
            mAllOrderView = (TextView)view.findViewById(R.id.tv_me_all_order);
            mDeliverView = (TextView)view.findViewById(R.id.tv_me_delivering);
            mCommentView = (TextView)view.findViewById(R.id.tv_me_commenting);
            mReturnView = (TextView)view.findViewById(R.id.tv_me_returning);
            mTabLineView = (ImageView)view.findViewById(R.id.iv_me_order_line);
            mOrderPager = (ViewPager)view.findViewById(R.id.vp_me_order);
        }
    }
}
