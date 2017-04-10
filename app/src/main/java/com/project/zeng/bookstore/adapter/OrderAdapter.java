package com.project.zeng.bookstore.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zeng.bookstore.R;
import com.project.zeng.bookstore.entities.Order;
import com.project.zeng.bookstore.entities.Result;
import com.project.zeng.bookstore.listeners.DataListener;
import com.project.zeng.bookstore.net.OrderAPI;
import com.project.zeng.bookstore.net.impl.OrderAPIImpl;
import com.project.zeng.bookstore.widgets.MyListView;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zeng on 2017/3/21.
 * MeFragment的订单的Adapter
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class OrderAdapter extends BaseAdapter {

    private Context mContext;
    private List<Order> mOrders;//订单列表
    private String mToken;//令牌

    private OrderProductAdapter mProductAdapter;//订单商品的Adapter
    //订单的网络请求API
    private OrderAPI mOrderAPI = new OrderAPIImpl();

    public OrderAdapter(Context context, String token) {
        this.mContext = context;
        mOrders = new ArrayList<>();
        mToken = token;
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
            viewHolder.mReceivedBtn = (Button) convertView.findViewById(R.id.btn_order_received);
            viewHolder.mReturnBtn = (Button) convertView.findViewById(R.id.btn_order_return);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Order item = mOrders.get(position);
        Picasso.with(mContext).load(item.getUser_img()).fit().into(viewHolder.mUserImgView);
        viewHolder.mUserNameView.setText(item.getUser_id());
        viewHolder.mOrderStateView.setText(item.getState());
        mProductAdapter = new OrderProductAdapter(mContext, item.getProducts());//为商品设置Adapter
        viewHolder.mProsListView.setAdapter(mProductAdapter);
        viewHolder.mProsCountView.setText("共" + item.getProducts().size() + "件商品");
        viewHolder.mProsTotalPriceView.setText("￥" + decimalFormat.format(item.getTotalprice()));
        initReceivedButton(viewHolder, item);
        return convertView;
    }

    //计算后，总价只保留小数点后两位
    private DecimalFormat decimalFormat = new DecimalFormat("#.##");

    /**
     * 初始化“确认收货”按钮及其事件
     * @param viewHolder
     * @param item
     */
    private void initReceivedButton(final ViewHolder viewHolder, final Order item){
        if(item.getState().equals("待发货")){
            viewHolder.mReceivedBtn.setVisibility(View.INVISIBLE);//隐藏该按钮
        }else if(item.getState().equals("待收货")){
            viewHolder.mReceivedBtn.setText("确认收货");
            viewHolder.mReceivedBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Log.e("OrderAdapter", "向服务器发送确认收货的信息");
                    mOrderAPI.modifyOrderState(mToken, item.getId(), 2, new DataListener<Result>() {
                        @Override
                        public void onComplete(Result result) {
                            if(result.getResult().contains("success")){
                                mOrders.remove(item);
                                notifyDataSetChanged();
                            }
                            Toast.makeText(mContext, result.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }else if(item.getState().equals("待评价")){
            viewHolder.mReceivedBtn.setText("评论");
            viewHolder.mReceivedBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.comment(item);
                }
            });
            viewHolder.mReturnBtn.setVisibility(View.VISIBLE);//显示退货按钮
            viewHolder.mReturnBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mReturnListener.returnReason(item);
                }
            });
        }else if(item.getState().equals("交易完成")){//已完成的订单
            viewHolder.mReceivedBtn.setVisibility(View.GONE);//隐藏该按钮
        }else{//退款
            viewHolder.mReceivedBtn.setVisibility(View.GONE);//隐藏该按钮
        }
    }

    private OnCommentClick mListener;

    public void setCommentListener(OnCommentClick listener) {
        this.mListener = listener;
    }

    /**
     * 评价的回调接口
     */
    public interface OnCommentClick{
        void comment(Order item);
    }

    private OnReturnClick mReturnListener;

    public void setReturnListener(OnReturnClick listener) {
        this.mReturnListener = listener;
    }

    /**
     * 退款的回调接口
     */
    public interface OnReturnClick{
        void returnReason(Order item);
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
    private static class ViewHolder{
        private ImageView mUserImgView;
        private TextView mUserNameView;
        private TextView mOrderStateView;
        private MyListView mProsListView;
        private TextView mProsCountView;
        private TextView mProsTotalPriceView;
        private Button mReceivedBtn;
        private Button mReturnBtn;
    }
}
