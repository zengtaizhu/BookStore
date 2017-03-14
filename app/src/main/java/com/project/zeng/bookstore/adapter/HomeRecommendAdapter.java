package com.project.zeng.bookstore.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import com.example.zeng.bookstore.R;
import com.project.zeng.bookstore.entities.Recommend;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zeng on 2017/2/28.
 */

public class HomeRecommendAdapter extends PagerAdapter{

    private Context mContext;

    private List<ImageView> mImageView;
    private int[] mImage = new int[]{R.drawable.books, R.drawable.totry};

    public HomeRecommendAdapter(Context mContext) {
        this.mContext = mContext;
        initData();
    }

    @Override
    public int getCount() {
        //设置成最大，使用户看不到边界
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if(mImageView.size() == 0)
            return null;
        position %= mImageView.size();
        if(position < 0){
            position = mImageView.size() + position;
        }
        ImageView imageView = mImageView.get(position);
        //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException
        ViewParent vp = imageView.getParent();
        if(vp != null){
            ViewGroup parent = (ViewGroup)vp;
            parent.removeView(imageView);
        }
        container.addView(imageView);
        return mImageView.get(position);
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
    }

    private void initData(){
        mImageView = new ArrayList<>();
        for(int imgID : mImage){
            ImageView imageView = new ImageView(mContext);
            imageView.setImageResource(imgID);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mImageView.add(imageView);
        }
    }

    public void updateData(List<Recommend> recommends){
        mImageView.clear();
        notifyDataSetChanged();
        for(final Recommend recommend : recommends){
            ImageView imageView = new ImageView(mContext);
            Picasso.with(mContext.getApplicationContext())
                    .load(recommend.getImgUrl())
                    .fit().into(imageView);
            mImageView.add(imageView);
        }
    }
}
