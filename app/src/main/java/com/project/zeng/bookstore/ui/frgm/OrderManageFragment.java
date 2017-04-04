package com.project.zeng.bookstore.ui.frgm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.zeng.bookstore.R;
import com.project.zeng.bookstore.adapter.OrderManageAdapter;
import com.project.zeng.bookstore.entities.Order;
import com.project.zeng.bookstore.listeners.DataListener;
import com.project.zeng.bookstore.net.OrderAPI;
import com.project.zeng.bookstore.net.impl.OrderAPIImpl;
import com.project.zeng.bookstore.ui.OrderDetailActivity;

import java.util.List;

/**
 * Created by zeng on 2017/4/4.
 * 订单管理的订单Fragment
 */

public class OrderManageFragment extends Fragment{

    private Context mContext;
    //组件
    private ListView mOrdersView;
    //适配器
    private OrderManageAdapter mOrderAdapter;

    //获得订单的网络请求API
    OrderAPI mOrderAPI = new OrderAPIImpl();

    private int mState;//当前的状态
    private String mToken;//令牌
    private int[] states = new int[]{0, 0, 3};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        mContext = getActivity().getApplication();
        mOrdersView = (ListView) view.findViewById(R.id.lv_order);
        initListener();
        return view;
    }

    /**
     * 初始化事件及其适配器
     */
    private void initListener() {
        mOrderAdapter = new OrderManageAdapter(mContext, mToken, new OrderManageAdapter.OnItemClickListener() {
            @Override
            public void onOrderClickListener(Order order) {
                Intent detailIntent = new Intent(mContext, OrderDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("order", order);
                bundle.putBoolean("isBuyer", false);
                detailIntent.putExtras(bundle);
                startActivityForResult(detailIntent, 0);
            }
        });
        mOrdersView.setAdapter(mOrderAdapter);
    }

    /**
     * 获取数据
     * @param state
     */
    public void fetchData(String token, int state){
        mState = state;
        mToken = token;
        if(token.equals("")){
            Toast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (state){
            //加载全部订单
            case 0:
                mOrderAPI.fetchOrders(token, new DataListener<List<Order>>() {
                    @Override
                    public void onComplete(List<Order> result) {
                        if(null != result){
                            mOrderAdapter.updateData(result);
                        }
                    }
                });
                break;
            //按照订单状态加载订单
            case 1:
            case 2:
            case 3:
                mOrderAPI.fetchOwnOrdersByState(token, states[state], new DataListener<List<Order>>() {
                    @Override
                    public void onComplete(List<Order> result) {
                        if(null != result){
                            mOrderAdapter.updateData(result);
                        }
                    }
                });
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 0){//跳转到订单详细页面OrderDetailActivity
            if(resultCode == 1){//修改了订单状态
                fetchData(mToken, mState);//重新刷新界面
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
