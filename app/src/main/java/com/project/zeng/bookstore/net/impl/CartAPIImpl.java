package com.project.zeng.bookstore.net.impl;

import android.util.Log;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.project.zeng.bookstore.entities.Cart;
import com.project.zeng.bookstore.listeners.DataListener;
import com.project.zeng.bookstore.net.CartAPI;
import com.project.zeng.bookstore.net.Handler.CartHandler;

import java.util.List;

/**
 * Created by zeng on 2017/3/18.
 * 购物车网络请求的实现类
 */

public class CartAPIImpl extends AbsNetwork<List<Cart>, String> implements CartAPI {

    private String url = "http://192.168.191.1:5000/api/v1.0/";

    public CartAPIImpl() {
        mRespHandler = new CartHandler();
    }

    /**
     * 通过买家ID，获得购物车列表
     * @param token
     * @param listener
     */
    @Override
    public void fetchCarts(String token, final DataListener<List<Cart>> listener) {
        String realUrl = url + "carts/" + token;
        StringRequest request = new StringRequest(realUrl, new Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(null != listener){
//                    Log.e("CartAPIImpl", "response=" + response);
                    listener.onComplete(mRespHandler.parse(response));
                }
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("CartAPIImpl", error.getMessage());
            }
        });
        //将网络请求添加到网络请求队列
        performRequest(request);
    }
}
