package com.project.zeng.bookstore.ui;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zeng.bookstore.R;
import com.project.zeng.bookstore.MyApplication;
import com.project.zeng.bookstore.adapter.OrderProductAdapter;
import com.project.zeng.bookstore.entities.Order;
import com.project.zeng.bookstore.entities.Result;
import com.project.zeng.bookstore.listeners.DataListener;
import com.project.zeng.bookstore.listeners.TextChangeListener;
import com.project.zeng.bookstore.net.OrderAPI;
import com.project.zeng.bookstore.net.impl.OrderAPIImpl;
import com.project.zeng.bookstore.widgets.MyListView;

/**
 * Created by zeng on 2017/4/4.
 * 订单详细页面Activity
 */

public class OrderDetailActivity extends Activity implements View.OnClickListener{

    private MyApplication app;//全局变量
    //组件
    private ImageView mBackView;
    private TextView mOrderIdView;
    private TextView mOrderStateView;
    private TextView mUserIdView;
    private TextView mUserPhoneView;
    private TextView mUserLocationView;
    private MyListView mOrderProsView;
    private TextView mOrderSendWayView;
    private RelativeLayout mOrderCourierLayout;
    private EditText mOrderCourierEdView;
    private TextView mBuyerCommentView;
    private EditText mBuyerCommentEdtView;
    private TextView mProsCountView;
    private TextView mOrderSubmitTimeView;
    private TextView mOrderProsPriceView;
    private TextView mOrderDeliverBtn;
    private TextView mOrderCommentBtn;
    private TextView mOrderReceiveBtn;

    //获得订单的网络请求API
    OrderAPI mOrderAPI = new OrderAPIImpl();
    private OrderProductAdapter mAdapter;//适配器
    private Order mOrder;//当前的订单
    private boolean isBuyer = false;//是否是卖家查看订单

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_order_detail);
        app = (MyApplication) getApplication();
        mOrder = (Order) getIntent().getExtras().get("order");
        isBuyer = getIntent().getExtras().getBoolean("isBuyer");
        init();
        initListener();
        initView();
    }

    /**
     * 初始化及组件
     */
    private void init(){
        mBackView = (ImageView) findViewById(R.id.iv_order_detail_back);
        mOrderIdView = (TextView) findViewById(R.id.tv_order_detail_id);
        mOrderStateView = (TextView) findViewById(R.id.tv_order_detail_state);
        mUserIdView = (TextView) findViewById(R.id.tv_order_detail_buyer_name);
        mUserPhoneView = (TextView) findViewById(R.id.tv_order_detail_buyer_phone);
        mUserLocationView = (TextView) findViewById(R.id.tv_order_detail_buyer_location);
        mOrderProsView = (MyListView) findViewById(R.id.lv_order_detail_pros);
        mOrderSendWayView = (TextView) findViewById(R.id.tv_order_detail_send_way);
        mOrderCourierLayout = (RelativeLayout) findViewById(R.id.rl_order_detail_courier);
        mOrderCourierEdView = (EditText) findViewById(R.id.et_order_detail_courier_id);
        mBuyerCommentView = (TextView) findViewById(R.id.tv_order_detail_comment);
        mBuyerCommentEdtView = (EditText) findViewById(R.id.et_order_detail_comment);
        mProsCountView = (TextView) findViewById(R.id.tv_order_detail_pro_count);
        mOrderSubmitTimeView = (TextView) findViewById(R.id.tv_order_detail_submit_time);
        mOrderProsPriceView = (TextView) findViewById(R.id.tv_order_detail_pros_price);
        mOrderDeliverBtn = (TextView) findViewById(R.id.tv_order_detail_deliver);
        mOrderCommentBtn = (TextView) findViewById(R.id.tv_order_detail_to_comment);
        mOrderReceiveBtn = (TextView) findViewById(R.id.tv_order_detail_receive);
    }

    /**
     * 初始化事件及其适配器
     */
    private void initListener(){
        mBackView.setOnClickListener(this);
        mOrderDeliverBtn.setOnClickListener(this);
        mOrderCommentBtn.setOnClickListener(this);
        mOrderReceiveBtn.setOnClickListener(this);
        mAdapter = new OrderProductAdapter(this, mOrder.getProducts());
        mOrderProsView.setAdapter(mAdapter);
    }

    /**
     * 初始化界面
     */
    private void initView(){
        mOrderIdView.setText("订单号:" + mOrder.getOrderId());
        mOrderStateView.setText(mOrder.getState());
        mUserIdView.setText("ID:" + mOrder.getUser_id());
        mUserPhoneView.setText(mOrder.getPhone());
        mUserLocationView.setText("收货地址:" + mOrder.getAddress());
        mOrderSendWayView.setText(mOrder.getSendWay());
        if(mOrder.getSendWay().equals("上门取货")){
            mOrderCourierLayout.setVisibility(View.GONE);//若不是快递，则隐藏快递单号
        }else{
            switch (mOrder.getState()){
                case "待发货":
                    mOrderCourierEdView.setEnabled(true);//允许输入
                    break;
                default:
                    mOrderCourierEdView.setText(mOrder.getCourier());//设置单号
                    break;
            }
        }
        switch (mOrder.getState()){
            case "待发货":
                if(!isBuyer){
                    mOrderDeliverBtn.setVisibility(View.VISIBLE);//卖家显示发货按钮
                }else{
                    mBuyerCommentEdtView.setEnabled(true);//买家允许输入留言
                }
                break;
            case "待收货":
                if(isBuyer){
                    mOrderReceiveBtn.setVisibility(View.VISIBLE);//买家显示收货按钮
                }
                break;
            case "待评价":
                if(isBuyer){
                    mOrderCommentBtn.setVisibility(View.INVISIBLE);//显示评价按钮
                }
                break;
            case "交易完成":
                //交易完成，都不显示
                mBuyerCommentView.setText("订单评价");
                if(isBuyer){
                    mBuyerCommentEdtView.setEnabled(true);//买家允许输入订单评价
                }
                break;
        }
        mBuyerCommentEdtView.setText(mOrder.getComment());
        mProsCountView.setText("共" + mOrder.getProducts().size() + "件商品");
        mOrderSubmitTimeView.setText("下单时间:" + mOrder.getSubmit_time());
        mOrderProsPriceView.setText("合计:" + mOrder.getTotalprice());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //返回按钮
            case R.id.iv_order_detail_back:
                finish();
                break;
            //评价按钮
            case R.id.tv_order_detail_to_comment:
                String comment = mBuyerCommentEdtView.getText().toString().trim();
                if(comment.equals("")){
                    Toast.makeText(OrderDetailActivity.this, "请输入订单的评价!", Toast.LENGTH_SHORT).show();
                }else{
                    mOrderAPI.commentOrder(app.getToken(), mOrder.getId(), 3,comment, new DataListener<Result>() {
                        @Override
                        public void onComplete(Result result) {
                            Toast.makeText(OrderDetailActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                            if(result.getResult().equals("success")){
                                setResult(1);
                                finish();
                            }
                        }
                    });
                }
                break;
            //发货按钮
            case R.id.tv_order_detail_deliver:
                String courierId = mOrderCourierEdView.getText().toString().trim();
                if(courierId.equals("")){
                    Toast.makeText(OrderDetailActivity.this, "请输入快递单号!", Toast.LENGTH_SHORT).show();
                }else{
                    mOrder.setCourier(courierId);
                    mOrderAPI.modifyOrder(app.getToken(), mOrder, 1, new DataListener<Result>() {//1:代表已发货
                        @Override
                        public void onComplete(Result result) {
                            Toast.makeText(OrderDetailActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                            if(result.getResult().equals("success")){
                                setResult(1);
                                finish();
                            }
                        }
                    });
                }
                break;
            //收货按钮
            case R.id.tv_order_detail_receive:

                break;
        }
    }
}
