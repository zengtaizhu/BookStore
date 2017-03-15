package com.project.zeng.bookstore.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zeng.bookstore.R;
import com.jakewharton.utils.RecyclingPagerAdapter;
import com.project.zeng.bookstore.entities.Recommend;
import com.project.zeng.bookstore.listeners.OnItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by zeng on 2017/3/14.
 * 广告的Adapter
 */

public class RecommendAdapter extends RecyclingPagerAdapter {

    private Context mContext;
    private List<Recommend> mRecommends;
    private ViewPager mViewPager;
    OnItemClickListener<Recommend> mItemClickListener;

    public RecommendAdapter(ViewPager viewPager, List<Recommend> recommends) {
        this.mViewPager = viewPager;
        this.mRecommends = recommends;
        mContext = mViewPager.getContext();
    }

    private int getPosition(int position){
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup container) {
        ViewHolder viewHolder;
        final Recommend item = getItem(position);
//        Log.e("RecommendAdapter", "Recommend的id=" + item.getProId() + ",title=" + item.getProName());
        if(null == convertView){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_recommend, container, false);
            viewHolder.mProImgView = (ImageView)convertView.findViewById(R.id.iv_head_recm_img);
            viewHolder.mTitleTxtView = (TextView)convertView.findViewById(R.id.tv_head_recm_title);
            convertView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(null != mItemClickListener){
                        mItemClickListener.onClick(item);
                    }
                }
            });
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Picasso.with(container.getContext()).load(item.getImgUrl()).fit().into(viewHolder.mProImgView);
        viewHolder.mTitleTxtView.setText(item.getProName());
        return convertView;
    }

    private Recommend getItem(int position){
        return mRecommends.get(getPosition(position));
    }

    @Override
    public int getCount() {
        return mRecommends.size();
    }

    public void setOnItemClickListener(OnItemClickListener<Recommend> onItemClickListener) {
        this.mItemClickListener = onItemClickListener;
    }

    /**
     * 广告的ViewHolder
     */
    private static class ViewHolder{
        private ImageView mProImgView;
        private TextView mTitleTxtView;
    }
}
