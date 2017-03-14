package com.project.zeng.bookstore.net.impl;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.project.zeng.bookstore.entities.Recommend;
import com.project.zeng.bookstore.listeners.DataListener;
import com.project.zeng.bookstore.net.Handler.RecommendHandler;
import com.project.zeng.bookstore.net.RecommendAPI;


import java.util.List;

/**
 * Created by zeng on 2017/2/28.
 */

public class RecommendAPIImpl extends AbsNetwork<List<Recommend>, String> implements RecommendAPI {

    /**
     * 商品推荐URL
     */
    private String url = "http://192.168.191.1:5000/api/v1.0/products/recommend/";

    public RecommendAPIImpl() {
        mRespHandler = new RecommendHandler();
    }

    @Override
    public void fetchRecommends(final DataListener<List<Recommend>> listener) {
        StringRequest request = new StringRequest(url, new Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Log.e("recm_APIImpl", "response=" + response);
                if (listener != null) {
                    // 解析结果
                    listener.onComplete(mRespHandler.parse(response));
                }
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("RecommendVolleyError", error.getMessage());
            }
        });
        performRequest(request);
    }
}
