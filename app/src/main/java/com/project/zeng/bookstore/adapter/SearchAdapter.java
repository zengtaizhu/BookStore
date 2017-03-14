package com.project.zeng.bookstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.zeng.bookstore.R;
import com.project.zeng.bookstore.entities.Search;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zeng on 2017/3/2.
 */

public class SearchAdapter extends BaseAdapter{

    private Context mContext;
    private List<Search> mSearches;

    public SearchAdapter(Context context) {
        this.mContext = context;
        mSearches = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mSearches.size();
    }

    @Override
    public Object getItem(int position) {
        return mSearches.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mSearches.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(null == convertView){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_search, parent, false);
            viewHolder.mTextView = (TextView) convertView.findViewById(R.id.tv_search_history);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mTextView.setText(mSearches.get(position).getKey());
        convertView.setTag(viewHolder);
        return convertView;
    }

    /**
     * 更新数据，更新界面
     * @param searches
     */
    public void updateData(List<Search> searches){
        mSearches.clear();
        mSearches = searches;
        notifyDataSetChanged();
    }

    /**
     * 清空数据，更新界面
     */
    public void clearData(){
        mSearches.clear();
        notifyDataSetChanged();
    }

    /**
     * 搜索记录的ViewHolder
     */
    private static class ViewHolder{

        TextView mTextView;
    }
}
