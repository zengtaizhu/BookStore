package com.project.zeng.bookstore.net.mgr;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by zeng on 2017/2/25.
 * 网络请求队列的管理类
 */

public class RequestQueueMgr {

    static RequestQueue mRequestQueue;

    private RequestQueueMgr(){
    }

    /**
     * 初始化网络请求队列
     * @param context
     */
    public static void init(Context context){
        if(mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(context);
        }
    }

    public static RequestQueue getRequestQueue(){
        return mRequestQueue;
    }
}
