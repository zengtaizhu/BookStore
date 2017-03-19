package com.project.zeng.bookstore.net.impl;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.project.zeng.bookstore.entities.Product;
import com.project.zeng.bookstore.listeners.DataListener;
import com.project.zeng.bookstore.net.Handler.ProductHandler;
import com.project.zeng.bookstore.net.ProductAPI;

import java.util.List;

/**
 * Created by zeng on 2017/3/2.
 * 商品网络请求的实现类
 */

public class ProductAPIImpl extends AbsNetwork<List<Product>, String> implements ProductAPI{

    private static String url = "http://192.168.191.1:5000/api/v1.0/products/";

    public ProductAPIImpl() {
        mRespHandler = new ProductHandler();
    }

    @Override
    public void fetchProductByID(String proId, final DataListener<Product> listener) {
        String newUrl = url + proId;
        StringRequest request = new StringRequest(newUrl, new Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Log.e("ProductAPIImplByID", "response=" + response);
                if(listener != null){
                    listener.onComplete(ProductHandler.parseOne(response));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("ProductAPIImpl", error.getMessage());
            }
        });
        //将请求添加到网络请求队列中
        performRequest(request);
    }

    @Override
    public void fetchProductsByCategory(String categoryId, final DataListener<List<Product>> listener) {
        String newUrl = url + "category/" + categoryId;
        StringRequest request = new StringRequest(newUrl, new Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Log.e("ProductAPIImpl", "response=" + response);
                if(listener != null){
                    listener.onComplete(mRespHandler.parse(response));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("ProductAPIImpl", error.getMessage());
            }
        });
        //将请求添加到网络请求队列中
        performRequest(request);
    }

    @Override
    public void loadMore(String categoryId, DataListener<List<Product>> listener, Response.ErrorListener errorListener) {

    }

    @Override
    public void fetchProductsByWord(String word, final DataListener<List<Product>> listener) {
        String newUrl = url + "keyword/" + word;
        StringRequest request = new StringRequest(newUrl, new Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(null != listener){
//                    Log.e("ProductAPIImpl", "response=" + response);
                    listener.onComplete(mRespHandler.parse(response));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("ProductAPIImpl", error.getMessage());
            }
        });
        //将网络请求添加到队列中
        performRequest(request);
    }

    /**
     * 获得商品推荐
     * @param listener
     */
    @Override
    public void fetchRecommends(final DataListener<List<Product>> listener) {
        String newUrl = url + "recommend/";
        StringRequest request = new StringRequest(newUrl, new Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(null != listener){
//                    Log.e("ProductAPIImpl", "response=" + response);
                    listener.onComplete(mRespHandler.parse(response));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("ProductAPIImpl", error.getMessage());
            }
        });
        //将网络请求添加到队列中
        performRequest(request);
    }
}
