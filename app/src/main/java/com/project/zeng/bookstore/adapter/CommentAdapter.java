package com.project.zeng.bookstore.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.project.zeng.bookstore.entities.Comment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zeng on 2017/3/15.
 */

public class CommentAdapter extends BaseAdapter{

    private Context mContext;
    private List<Comment> mComments;

    public CommentAdapter(Context context) {
        this.mContext = context;
        mComments = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mComments.size();
    }

    @Override
    public Object getItem(int position) {
        return mComments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.valueOf(mComments.get(position).getId());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    /**
     * 更新数据并更新界面
     * @param comments
     */
    public void updateData(List<Comment> comments){
        mComments.clear();
        mComments = comments;
        notifyDataSetChanged();
    }

    private static class ViewHolder{
        private TextView mUserIdTxtView;
        private TextView mCommentGradeTxtView;
        private TextView mCommentTimeTxtView;
        private TextView mCommentTxtView;
    }
}
