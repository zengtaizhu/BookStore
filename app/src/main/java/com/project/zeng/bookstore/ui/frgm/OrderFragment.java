package com.project.zeng.bookstore.ui.frgm;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.zeng.bookstore.R;
import com.project.zeng.bookstore.MyApplication;
import com.project.zeng.bookstore.adapter.OrderAdapter;
import com.project.zeng.bookstore.entities.Order;
import com.project.zeng.bookstore.listeners.DataListener;
import com.project.zeng.bookstore.net.OrderAPI;
import com.project.zeng.bookstore.net.impl.OrderAPIImpl;

import java.util.List;

/**
 * Created by zeng on 2017/3/21.
 * MeFragment的订单Fragment
 */

public class OrderFragment extends Fragment{

    //全局变量
    private MyApplication app;

    private Context mContext;
    //组件
    private ListView mOrdersView;
    //适配器
    private OrderAdapter mOrderAdapter;

    //获得订单的网络请求API
    OrderAPI mOrderAPI = new OrderAPIImpl();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        mContext = getActivity().getApplicationContext();
        app = (MyApplication) mContext;
//        Log.e("OrderFragment", "token=" + app.getToken());
        init(view);
        initListener();
        return view;
    }

    /**
     * 初始化组件
     * @param view
     */
    private void init(View view) {
        mOrdersView = (ListView) view.findViewById(R.id.lv_order);
    }

    /**
     * 初始化事件及其适配器
     */
    private void initListener() {
        mOrderAdapter = new OrderAdapter(mContext);
        mOrdersView.setAdapter(mOrderAdapter);
    }

    /**
     * 根据state的值，获取相应状态的订单
     */
    public void fetchData(int state) {
        switch (state){
            //全部
            case 0:
                mOrderAPI.fetchOrders(app.getToken(), new DataListener<List<Order>>() {
                    @Override
                    public void onComplete(List<Order> result) {
                        if(null != result){
//                            Log.e("OrderFragment", "从网络加载的Order的数量为：" + result.size());
                            mOrderAdapter.updateData(result);
                        }
                    }
                });
                break;
            //其他三种状态
            case 1:
            case 2:
            case 3:
                mOrderAPI.fetchOrdersByState(app.getToken(), state, new DataListener<List<Order>>() {
                    @Override
                    public void onComplete(List<Order> result) {
                        if(null != result){
//                            Log.e("OrderFragment", "从网络加载的Order的数量为：" + result.size());
                            mOrderAdapter.updateData(result);
                        }
                    }
                });
                break;
        }
    }
}
