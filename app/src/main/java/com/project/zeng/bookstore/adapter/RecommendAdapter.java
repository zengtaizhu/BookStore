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
import com.project.zeng.bookstore.entities.Product;
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
    private List<Product> mProducts;
    private ViewPager mViewPager;
    OnItemClickListener<Product> mItemClickListener;

    public RecommendAdapter(ViewPager viewPager, List<Product> products) {
        this.mViewPager = viewPager;
        this.mProducts = products;
        mContext = mViewPager.getContext();
    }

    private int getPosition(int position){
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup container) {
        ViewHolder viewHolder;
        final Product item = getItem(position);
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
        Picasso.with(container.getContext()).load(item.getPictureUrl()).fit().into(viewHolder.mProImgView);
        viewHolder.mTitleTxtView.setText(item.getTitle());
        return convertView;
    }

    private Product getItem(int position){
        return mProducts.get(getPosition(position));
    }

    @Override
    public int getCount() {
        return mProducts.size();
    }

    public void setOnItemClickListener(OnItemClickListener<Product> onItemClickListener) {
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
