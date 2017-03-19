package com.project.zeng.bookstore.net.Handler;


import com.google.gson.Gson;

/**
 * Created by zeng on 2017/2/25.
 * 网络响应的处理接口
 */

public interface RespHandler<T, D> {

    public Gson gson = new Gson();
    /**
     * 将从网络获取的数据解析成对象
     * @param data
     * @return
     */
    public T parse(D data);
}
