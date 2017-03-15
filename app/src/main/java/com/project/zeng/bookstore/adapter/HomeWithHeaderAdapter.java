package com.project.zeng.bookstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zeng.bookstore.R;
import com.project.zeng.bookstore.entities.Recommend;
import com.project.zeng.bookstore.listeners.OnItemClickListener;
import com.project.zeng.bookstore.widgets.AutoScrollViewPager;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.*;

/**
 * Created by zeng on 2017/3/14.
 * 广告栏的Adapter
 */

public class HomeWithHeaderAdapter extends Adapter<ViewHolder>{

    private Context mContext;

    OnItemClickListener<Recommend> mRecommendListener;
    List<Recommend> mRecommends;
    //广告栏的Adapter
    HeaderViewHolder mHeaderViewHolder;

    //广告栏上广告的Adapter
    RecommendAdapter mRecommendAdapter;

    public HomeWithHeaderAdapter(Context context) {
        this.mContext = context;
        mRecommends = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.auto_slider, parent, false);
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mHeaderViewHolder = (HeaderViewHolder)holder;
        AutoScrollViewPager viewPager = mHeaderViewHolder.mAutoScrollViewPager;
        mRecommendAdapter = new RecommendAdapter(viewPager, mRecommends);
        mRecommendAdapter.setOnItemClickListener(mRecommendListener);
        viewPager.setInterval(4000);//播放间隔4s
        //设置ViewPager
        if(mRecommends.size() > 0){
            viewPager.startAutoScroll();
            viewPager.setCurrentItem(position % mRecommends.size());
            viewPager.setAdapter(mRecommendAdapter);
            mHeaderViewHolder.mIndicator.setViewPager(viewPager);
            setupItemViewClickListener(holder, mRecommends.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mRecommends.size();
    }

    /**
     * 更新数据并开始自动播放
     * @param recommends
     */
    public void updateData(List<Recommend> recommends){
        mRecommends.clear();
        mRecommends = recommends;
        notifyDataSetChanged();
    }

    public void setRecommendClickListener(OnItemClickListener<Recommend> recommendListener) {
        this.mRecommendListener = recommendListener;
    }

    /**
     * 广告ItemView的点击事件
     * @param viewHolder
     * @param item
     */
    public void setupItemViewClickListener(ViewHolder viewHolder, final Recommend item){
        viewHolder.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mRecommendListener != null){
                    mRecommendListener.onClick(item);
                }
            }
        });
    }

    /**
     * 顶部的自动滚动的Recommend的ViewHolder
     */
    public static class HeaderViewHolder extends ViewHolder{
        public AutoScrollViewPager mAutoScrollViewPager;
        public CirclePageIndicator mIndicator;

        public HeaderViewHolder(View view) {
            super(view);
            mAutoScrollViewPager = (AutoScrollViewPager)view.findViewById(R.id.viewpager_home_recommend);
            mIndicator = (CirclePageIndicator)view.findViewById(R.id.indicator_home_recm_title);
        }
    }
}
