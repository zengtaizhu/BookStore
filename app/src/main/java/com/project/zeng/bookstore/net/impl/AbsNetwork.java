package com.project.zeng.bookstore.net.impl;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.project.zeng.bookstore.net.Handler.RespHandler;
import com.project.zeng.bookstore.net.mgr.RequestQueueMgr;

/**
 * Created by zeng on 2017/2/25.
 * 网络抽象接口
 */

public abstract class AbsNetwork<T, D> {

    static RequestQueue sRequestQueue = RequestQueueMgr.getRequestQueue();

    RespHandler<T, D> mRespHandler;

    /**
     * 执行网络请求
     * @param request
     */
    protected void performRequest(Request<?> request){
        sRequestQueue.add(request);
    }
}
