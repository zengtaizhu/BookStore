package com.project.zeng.bookstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zeng.bookstore.R;
import com.project.zeng.bookstore.entities.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zeng on 2017/3/1.
 */

public class CategoryProductAdapter extends BaseAdapter{

    private Context mContext;
    private List<Product> mProducts;

    public CategoryProductAdapter(Context context) {
        this.mContext = context;
        mProducts = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mProducts.size();
    }

    @Override
    public Object getItem(int position) {
        return mProducts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Integer.valueOf(mProducts.get(position).getId());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(null == convertView){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_category_product, parent, false);
            viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.iv_category_pro_img);
            viewHolder.mTextView = (TextView) convertView.findViewById(R.id.tv_category_pro_title);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        //设置内容
        Picasso.with(mContext).load(mProducts.get(position).getPictureUrl()).fit().into(viewHolder.mImageView);
        viewHolder.mTextView.setText(mProducts.get(position).getTitle());
        return convertView;
    }


    /**
     * 更新数据
     * @param products
     */
    public void updataData(List<Product> products){
        mProducts.clear();
        mProducts = products;
        notifyDataSetChanged();
    }

    /**
     * 分类的Product的ViewHolder
     */
    private static class ViewHolder{

        public ImageView mImageView;
        public TextView mTextView;
    }
}
