package com.project.zeng.bookstore.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zeng.bookstore.R;
import com.project.zeng.bookstore.entities.Comment;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by zeng on 2017/3/15.
 * ProductDetailActivity的评论的Adapter
 */

public class CommentAdapter extends BaseAdapter{

    private Context mContext;
    private List<Comment> mComments;//评论列表

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
        return Integer.valueOf(mComments.get(position).getId());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(null == convertView){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_comment, parent, false);
            viewHolder.mUserImgView = (ImageView) convertView.findViewById(R.id.iv_comment_user_img);
            viewHolder.mUserIdView = (TextView)convertView.findViewById(R.id.tv_comment_user_id);
            viewHolder.mCommentTimeView = (TextView)convertView.findViewById(R.id.tv_comment_time);
            viewHolder.mCommentView = (TextView)convertView.findViewById(R.id.tv_comment);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Picasso.with(mContext).load(mComments.get(position).getBuyer_img()).fit().into(viewHolder.mUserImgView );
        viewHolder.mUserIdView.setText(mComments.get(position).getBuyer_id());
        viewHolder.mCommentTimeView.setText(mComments.get(position).getComment_time());
        viewHolder.mCommentView.setText(mComments.get(position).getComment());
        return convertView;
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
        private ImageView mUserImgView;
        private TextView mUserIdView;
        private TextView mCommentTimeView;
        private TextView mCommentView;
    }
}
