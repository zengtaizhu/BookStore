package com.project.zeng.bookstore.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zeng.bookstore.R;
import com.project.zeng.bookstore.entities.Order;
import com.project.zeng.bookstore.widgets.MyListView;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zeng on 2017/3/21.
 * MeFragment的订单的Adapter
 */

public class OrderAdapter extends BaseAdapter {

    private Context mContext;
    private List<Order> mOrders;

    private OrderProductAdapter mProductAdapter;

    public OrderAdapter(Context context) {
        this.mContext = context;
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
        return Long.valueOf(mOrders.get(position).getId());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_order, parent, false);
            viewHolder.mUserImgView = (ImageView) convertView.findViewById(R.id.iv_order_user_img);
            viewHolder.mUserNameView = (TextView) convertView.findViewById(R.id.tv_order_username);
            viewHolder.mOrderStateView = (TextView) convertView.findViewById(R.id.tv_order_state);
            viewHolder.mProsListView = (MyListView) convertView.findViewById(R.id.lv_order_product);
            viewHolder.mProsCountView = (TextView) convertView.findViewById(R.id.tv_order_pro_total);
            viewHolder.mProsTotalPriceView = (TextView) convertView.findViewById(R.id.tv_order_pro_total_price);
            viewHolder.mOrderDetailBtn = (Button) convertView.findViewById(R.id.btn_order_detail);
            viewHolder.mReceivedBtn = (Button) convertView.findViewById(R.id.btn_order_received);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Order item = mOrders.get(position);
        Picasso.with(mContext).load(item.getSeller_img()).fit().into(viewHolder.mUserImgView);
        viewHolder.mUserNameView.setText(item.getSeller_id());
        viewHolder.mOrderStateView.setText(item.getState());
        mProductAdapter = new OrderProductAdapter(mContext, item.getProducts());//为商品设置Adapter
        viewHolder.mProsListView.setAdapter(mProductAdapter);
        viewHolder.mProsCountView.setText("共" + item.getProducts().size() + "件商品");
        viewHolder.mProsTotalPriceView.setText("￥" + decimalFormat.format(item.getTotalprice()));
        initReceivedButton(viewHolder, item);
        viewHolder.mOrderDetailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("OrderAdapter", "查看订单详情");
            }
        });
        return convertView;
    }

    //计算后，总价只保留小数点后两位
    private DecimalFormat decimalFormat = new DecimalFormat(".##");

    /**
     * 初始化“确认收货”按钮及其事件
     * @param viewHolder
     * @param item
     */
    private void initReceivedButton(ViewHolder viewHolder, Order item){
        if(item.getState().equals("待收货")){
            viewHolder.mReceivedBtn.setText("确认收货");
            viewHolder.mReceivedBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("OrderAdapter", "向服务器发送确认收货的信息");
                }
            });
        }else if(item.getState().equals("待评价")){
            viewHolder.mReceivedBtn.setText("评论");
            viewHolder.mReceivedBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("OrderAdapter", "向服务器发送确认收货的信息");
                }
            });
        }else if(item.getState().equals("退款")){
            viewHolder.mReceivedBtn.setVisibility(View.INVISIBLE);//隐藏该按钮
            viewHolder.mOrderDetailBtn.setGravity(RelativeLayout.ALIGN_PARENT_LEFT);//将查看详情的按钮置右
        }
    }

    /**
     * 更新数据，刷新界面
     * @param orders
     */
    public void updateData(List<Order> orders){
        mOrders.clear();
        mOrders = orders;
        notifyDataSetChanged();
    }

    /**
     * 订单的ViewHolder
     */
    private static class ViewHolder{
        private ImageView mUserImgView;
        private TextView mUserNameView;
        private TextView mOrderStateView;
        private MyListView mProsListView;
        private TextView mProsCountView;
        private TextView mProsTotalPriceView;
        private Button mOrderDetailBtn;
        private Button mReceivedBtn;
    }
}
