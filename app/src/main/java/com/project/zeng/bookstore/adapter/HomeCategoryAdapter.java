package com.project.zeng.bookstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zeng.bookstore.R;
import com.project.zeng.bookstore.entities.Category;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zeng on 2017/3/1.
 */

public class HomeCategoryAdapter extends BaseAdapter{

    private Context mContext;

    private List<Category> mCategories;

    public HomeCategoryAdapter(Context context){
        mContext = context;
        mCategories = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mCategories.size();
    }

    @Override
    public Object getItem(int position) {
        return mCategories.get(position);
    }

    @Override
    public long getItemId(int position) {
        //将Category的ID作为ItemId
        return Integer.valueOf(mCategories.get(position).getId());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(null == convertView){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.item_home_category, parent, false);
            viewHolder.categoryTextView = (TextView)convertView.findViewById(R.id.tv_home_category_name);
            viewHolder.describeTextView = (TextView)convertView.findViewById(R.id.tv_home_category_describe);
            viewHolder.imageView = (ImageView)convertView.findViewById(R.id.iv_home_category_img);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        //设置内容
        viewHolder.categoryTextView.setText(mCategories.get(position).getName());
        viewHolder.describeTextView.setText(mCategories.get(position).getDescribe());
        //作为缓冲的作用
        viewHolder.imageView.setBackgroundResource(R.mipmap.ic_launcher);
        Picasso.with(mContext).load(mCategories.get(position).getPictureUrl()).fit().into(viewHolder.imageView);
        return convertView;
    }

    /**
     * 更新数据
     * @param categories
     */
    public void updateData(List<Category> categories){
        mCategories = categories;
        notifyDataSetChanged();
    }

    /**
     * 首页的Category的ViewHolder
     */
    private static class ViewHolder{

        public TextView categoryTextView;
        public TextView describeTextView;
        public ImageView imageView;
    }
}
