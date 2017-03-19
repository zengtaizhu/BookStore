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
import com.project.zeng.bookstore.listeners.OnItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zeng on 2017/3/1.
 * 分类Fragment的商品Adapter
 */

public class CategoryProductAdapter extends BaseAdapter{

    private Context mContext;
    private List<Product> mProducts;

    private OnItemClickListener<Product> mItemClickListener;

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
        final Product item = mProducts.get(position);
        ViewHolder viewHolder;
        if(null == convertView){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_category_product, parent, false);
            viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.iv_category_pro_img);
            viewHolder.mTextView = (TextView) convertView.findViewById(R.id.tv_category_pro_title);
            //为每一个商品添加点击事件
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mItemClickListener != null){
                        mItemClickListener.onClick(item);
                    }
                }
            });
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        //设置内容
        Picasso.with(mContext).load(item.getPictureUrl()).fit().into(viewHolder.mImageView);
        viewHolder.mTextView.setText(item.getTitle());
        return convertView;
    }


    /**
     * 更新数据
     * @param products
     */
    public void updateData(List<Product> products){
        mProducts.clear();
        mProducts = products;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener<Product> listener){
        this.mItemClickListener = listener;
    }

    /**
     * 分类的Product的ViewHolder
     */
    private static class ViewHolder{
        private ImageView mImageView;
        private TextView mTextView;
    }
}
