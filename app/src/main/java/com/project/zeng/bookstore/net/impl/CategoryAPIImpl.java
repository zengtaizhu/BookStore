package com.project.zeng.bookstore.net.impl;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.project.zeng.bookstore.entities.Category;
import com.project.zeng.bookstore.listeners.DataListener;
import com.project.zeng.bookstore.net.CategoryAPI;
import com.project.zeng.bookstore.net.Handler.CategoryHandler;

import java.util.List;

/**
 * Created by zeng on 2017/3/1.
 * 商品类型网络请求的实现类
 */

public class CategoryAPIImpl extends AbsNetwork<List<Category>, String> implements CategoryAPI {

    private static String url = "http://192.168.191.1:5000/api/v1.0/categories/";

    public CategoryAPIImpl() {
        mRespHandler = new CategoryHandler();
    }

    public static void setUrl(String url) {
        CategoryAPIImpl.url = url;
    }

    @Override
    public void fetchCategories(final DataListener<List<Category>> listener) {
        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.e("categoryAPIImpl", "response=" + response);
                        if(listener != null){
                            //解析结果
                            listener.onComplete(mRespHandler.parse(response));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("categoryAPIImpl", error.getMessage());
                listener.onComplete(null);
            }
        });
        //将请求添加到网络请求队列中
        performRequest(request);
    }
}
