package com.project.zeng.bookstore.net.Handler;

import com.google.gson.Gson;
import com.project.zeng.bookstore.entities.Result;
import com.project.zeng.bookstore.entities.User;

/**
 * Created by zeng on 2017/3/8.
 * 网络响应的处理Handler
 */

public class ResultHandler{

    /**
     * 用GSON解析出结果
     * @param data
     * @return
     */
    public static Result getResult(String data){
        Gson gson = new Gson();
        return gson.fromJson(data, Result.class);
    }
}
