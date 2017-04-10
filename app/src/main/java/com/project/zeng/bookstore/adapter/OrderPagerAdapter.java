package com.project.zeng.bookstore.adapter;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.database.DataSetObserver;
import android.os.Build;
import android.support.annotation.RequiresApi;
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
    private int mScreenOneOfFive = 0;
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
        mScreenOneOfFive = mParentFrgm.getScreenWidth() / 5;
        initTabLine();
    }

    /**
     * 初始化Fragment数组，即五个ViewPager的页面
     */
    private void initFragments(){
        mFragments = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            mFragments.add(new OrderFragment());
        }
    }

    /**
     * 初始化TabLine，使其宽度为屏幕的五分之一
     */
    private void initTabLine(){
        ViewGroup.LayoutParams layoutParams = mViewHolder.mTabLineView.getLayoutParams();
        layoutParams.width = mScreenOneOfFive;
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
//        Log.e("OrderPagerAdapter", "position=" + position);
        OrderFragment fragment = mFragments.get(position);
        if(!fragment.isAdded()){//若未添加，则将Fragment添加到ViewPager中
            FragmentTransaction ft = mFrgmManager.beginTransaction();
            ft.add(fragment, fragment.getClass().getSimpleName());
            ft.commit();
            mFrgmManager.executePendingTransactions();//立即执行添加操作
            fragment.fetchData(position);
        }
        if(fragmentChange[position % fragmentChange.length]){
            FragmentTransaction ft = mFrgmManager.beginTransaction();
            ft.remove(fragment);
            ft.add(fragment, fragment.getClass().getSimpleName());
            ft.commit();
            mFrgmManager.executePendingTransactions();//立即执行添加操作
            fragment.fetchData(position);//重新获取数据
            fragmentChange[position % fragmentChange.length] = false;//将修改复原
        }
        if(null == fragment.getView().getParent()){
            container.addView(fragment.getView());//为ViewPager添加布局
        }
        return fragment.getView();
    }

    private boolean[] fragmentChange = {false, false, false, false, false};

    /**
     * 更新界面
     */
    public void updateView(){
        for(int i = 0; i < fragmentChange.length; i++){
            fragmentChange[i] = true;
        }
        notifyDataSetChanged();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mViewHolder.mTabLineView.getLayoutParams();
        lp.leftMargin = (int)(currentPageIndex * mScreenOneOfFive + (positionOffset + position - currentPageIndex) * mScreenOneOfFive);
        mViewHolder.mTabLineView.setLayoutParams(lp);//设置TabLine的右偏移量
    }

    @Override
    public void onPageSelected(int position) {
        //将ViewPager上的订单状态的TextView设置为默认颜色
        resetTextView();
        //将选中的页码对于的TextView设置为红色
        switch (position){
            //待发货订单
            case 0:
                mViewHolder.mDeliverView.setTextColor(mParentFrgm.getResources().getColor(R.color.text_red));
                break;
            //待收货订单
            case 1:
                mViewHolder.mReceiveView.setTextColor(mParentFrgm.getResources().getColor(R.color.text_red));
                break;
            //待评价订单
            case 2:
                mViewHolder.mCommentView.setTextColor(mParentFrgm.getResources().getColor(R.color.text_red));
                break;
            //交易完成的订单
            case 3:
                mViewHolder.mOrderDoneView.setTextColor(mParentFrgm.getResources().getColor(R.color.text_red));
                break;
            case 4:
                mViewHolder.mReturnView.setTextColor(mParentFrgm.getResources().getColor(R.color.text_red));
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
        mViewHolder.mDeliverView.setTextColor(mParentFrgm.getResources().getColor(R.color.text_black));
        mViewHolder.mReceiveView.setTextColor(mParentFrgm.getResources().getColor(R.color.text_black));
        mViewHolder.mCommentView.setTextColor(mParentFrgm.getResources().getColor(R.color.text_black));
        mViewHolder.mOrderDoneView.setTextColor(mParentFrgm.getResources().getColor(R.color.text_black));
        mViewHolder.mReturnView.setTextColor(mParentFrgm.getResources().getColor(R.color.text_black));
    }

    /**
     * MeFragment的订单ViewPager的ViewHolder
     */
    private static class ViewHolder{

        private TextView mDeliverView;
        private TextView mCommentView;
        private TextView mReceiveView;
        private TextView mOrderDoneView;
        private TextView mReturnView;
        private ImageView mTabLineView;
        private ViewPager mOrderPager;

        public ViewHolder(View view) {
            mDeliverView = (TextView)view.findViewById(R.id.tv_me_delivering);
            mReceiveView = (TextView) view.findViewById(R.id.tv_me_receiving);
            mCommentView = (TextView)view.findViewById(R.id.tv_me_commenting);
            mOrderDoneView = (TextView)view.findViewById(R.id.tv_me_done_order);
            mReturnView = (TextView) view.findViewById(R.id.tv_me_returning);
            mTabLineView = (ImageView)view.findViewById(R.id.iv_me_order_line);
            mOrderPager = (ViewPager)view.findViewById(R.id.vp_me_order);
        }
    }
}
