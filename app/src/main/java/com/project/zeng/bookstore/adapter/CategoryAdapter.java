package com.project.zeng.bookstore.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.zeng.bookstore.R;
import com.project.zeng.bookstore.entities.Category;
import com.project.zeng.bookstore.ui.frgm.CategoryFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zeng on 2017/3/1.
 */

public class CategoryAdapter extends BaseAdapter {

    private Context mContext;
    private List<Category> mCategories;

    public CategoryAdapter(Context context){
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
        return Integer.valueOf(mCategories.get(position).getId());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(null == convertView){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_category, parent, false);
            viewHolder.mTextView = (TextView) convertView.findViewById(R.id.tv_category_name);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        //设置界面
        viewHolder.mTextView.setText(mCategories.get(position).getName());
        if(position == CategoryFragment.mPosition){
            convertView.setBackgroundColor(Color.WHITE);
            viewHolder.mTextView.setTextColor(Color.parseColor("#ea5e68"));
        }else{
            convertView.setBackgroundResource(R.drawable.category_bg);
            viewHolder.mTextView.setTextColor(Color.parseColor("#000000"));
        }
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
     * 商品类型的ViewHolder
     */
    private static class ViewHolder{
        public TextView mTextView;
    }
}
