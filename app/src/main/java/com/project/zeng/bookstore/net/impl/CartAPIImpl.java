package com.project.zeng.bookstore.net.impl;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.project.zeng.bookstore.entities.Cart;
import com.project.zeng.bookstore.entities.Result;
import com.project.zeng.bookstore.listeners.DataListener;
import com.project.zeng.bookstore.net.CartAPI;
import com.project.zeng.bookstore.net.Handler.CartHandler;
import com.project.zeng.bookstore.net.Handler.UserHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * 通过令牌，获得购物车列表
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

    /**
     * 通过令牌，将商品ID为proId的商品添加到购物车中
     * @param token
     * @param proId
     * @param listener
     */
    @Override
    public void addProToCart(final String token, final String proId, final DataListener<Result> listener){
        String realUrl = url + "carts/add/";
        StringRequest request = new StringRequest(Request.Method.POST, realUrl, new Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(null != response){
//                    Log.e("UserAPIImpl", "response=" + response);
                    listener.onComplete(UserHandler.getResult(response));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("UserAPIImpl", error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                params.put("id", proId);
                return params;
            }
        };
        //将请求添加到网络请求队列中
        performRequest(request);
    }
}
