package com.project.zeng.bookstore.net.impl;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.project.zeng.bookstore.entities.Order;
import com.project.zeng.bookstore.listeners.DataListener;
import com.project.zeng.bookstore.net.Handler.OrderHandler;
import com.project.zeng.bookstore.net.OrderAPI;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zeng on 2017/3/22.
 */

public class OrderAPIImpl extends AbsNetwork<List<Order> ,String> implements OrderAPI {

    private String url = "http://192.168.191.1:5000/api/v1.0/";

    public OrderAPIImpl() {
        mRespHandler = new OrderHandler();
    }

    /**
     * 通过令牌Token，获得订单列表
     * @param token
     * @param listener
     */
    @Override
    public void fetchOrders(String token, final DataListener<List<Order>> listener) {
        String realUrl = url + "orders/user/" + token;
        StringRequest request = new StringRequest(realUrl, new Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(listener != null){
//                    Log.e("OrderAPIImpl", "token:response=" + response);
                    listener.onComplete(mRespHandler.parse(response));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("OrderAPIImpl", error.getMessage());
            }
        });
        //将网络请求添加到网络请求队列
        performRequest(request);
    }

    /**
     * 通过Token令牌获取符合state订单状态的订单
     * @param token
     * @param state
     * @param listener
     */
    @Override
    public void fetchOrdersByState(final String token, final int state, final DataListener<List<Order>> listener) {
        String realUrl = url + "orders/user/";
        StringRequest request = new StringRequest(Request.Method.POST, realUrl, new Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(listener != null){
//                    Log.e("OrderAPIImpl", "state:response=" + response);
                    listener.onComplete(mRespHandler.parse(response));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("OrderAPIImpl", error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("state", ORDER_STATE[state]);
                params.put("token", token);
//                Log.e("OrderAPIImpl", "token=" + token + ",state=" + ORDER_STATE[state]);
                return params;
            }
        };
        //将网络请求添加到网络请求队列
        performRequest(request);
    }
}
