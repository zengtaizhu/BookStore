package com.project.zeng.bookstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zeng.bookstore.R;
import com.project.zeng.bookstore.entities.Order;
import com.project.zeng.bookstore.widgets.MyListView;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zeng on 2017/4/4.
 * OrderManageActivity的订单Fragment
 */

public class OrderManageAdapter extends BaseAdapter{

    private Context mContext;
    private List<Order> mOrders;//订单列表
    private String mToken;//令牌
    private OrderProductAdapter mProductAdapter;//订单商品的Adapter
    private OnItemClickListener mListener;

    public OrderManageAdapter(Context mContext, String mToken, OnItemClickListener listener) {
        this.mContext = mContext;
        this.mToken = mToken;
        mListener = listener;
        mOrders = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mOrders.size();
    }

    @Override
    public Object getItem(int position) {
        return mOrders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Integer.valueOf(mOrders.get(position).getId());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if(convertView == null){
            mViewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_order_manage, null);
            mViewHolder.mUserImgView = (ImageView) convertView.findViewById(R.id.iv_order_manage_user_img);
            mViewHolder.mUserIdView = (TextView) convertView.findViewById(R.id.tv_order_manage_username);
            mViewHolder.mOrderStateView = (TextView) convertView.findViewById(R.id.tv_order_manage_state);
            mViewHolder.mProsView = (MyListView) convertView.findViewById(R.id.lv_order_manage_products);
            mViewHolder.mProsCountView = (TextView) convertView.findViewById(R.id.tv_order_manage_pro_total);
            mViewHolder.mProsPriceView = (TextView) convertView.findViewById(R.id.tv_order_manage_pros_price);
            mViewHolder.mOrderDetailBtn = (Button) convertView.findViewById(R.id.btn_order_manage_detail);
            convertView.setTag(mViewHolder);
        }else{
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        final Order item = mOrders.get(position);
        Picasso.with(mContext).load(item.getUser_img()).fit().into(mViewHolder.mUserImgView);
        mViewHolder.mUserIdView.setText(item.getUser_id());
        mViewHolder.mOrderStateView.setText(item.getState());
        mProductAdapter = new OrderProductAdapter(mContext, item.getProducts());
        mViewHolder.mProsView.setAdapter(mProductAdapter);
        mViewHolder.mProsCountView.setText("共" + item.getProducts().size() + "件商品");
        mViewHolder.mProsPriceView.setText("￥" + decimalFormat.format(item.getTotalprice()));
        mViewHolder.mOrderDetailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onOrderClickListener(item);
            }
        });
        return convertView;
    }

    //计算后，总价只保留小数点后两位
    private DecimalFormat decimalFormat = new DecimalFormat("#.##");

    public interface OnItemClickListener{
        void onOrderClickListener(Order order);
    }

    /**
     * 更新数据，刷新界面
     * @param orders
     */
    public void updateData(List<Order> orders){
        mOrders = orders;
        notifyDataSetChanged();
    }

    /**
     * 订单的ViewHolder
     */
    public static class ViewHolder{
        private ImageView mUserImgView;
        private TextView mUserIdView;
        private TextView mOrderStateView;
        private MyListView mProsView;
        private TextView mProsCountView;
        private TextView mProsPriceView;
        private Button mOrderDetailBtn;
    }
}
