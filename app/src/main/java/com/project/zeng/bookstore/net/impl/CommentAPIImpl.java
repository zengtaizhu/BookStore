package com.project.zeng.bookstore.net.impl;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.project.zeng.bookstore.entities.Comment;
import com.project.zeng.bookstore.listeners.DataListener;
import com.project.zeng.bookstore.net.CommentAPI;
import com.project.zeng.bookstore.net.Handler.CommentHandler;

import java.util.List;

/**
 * Created by zeng on 2017/3/16.
 */

public class CommentAPIImpl extends AbsNetwork<List<Comment>, String> implements CommentAPI{

    private String url = "http://192.168.191.1:5000/api/v1.0/products/comment";

    public CommentAPIImpl() {
        mRespHandler = new CommentHandler();
    }

    /**
     * 通过商品ID，获得商品评论列表
     * @param proId
     * @param listener
     */
    @Override
    public void fetchComment(String proId, final DataListener<List<Comment>> listener) {
        String realUrl = url + "/" + proId;
        StringRequest request = new StringRequest(realUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Log.e("CommentAPIImpl", "response=" + response);
                if(null != listener){
                    listener.onComplete(mRespHandler.parse(response));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("CommentAPIImpl", error.getMessage());
            }
        });
        //将网络请求添加到网络请求队列
        performRequest(request);
    }
}
