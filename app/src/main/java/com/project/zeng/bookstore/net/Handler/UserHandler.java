package com.project.zeng.bookstore.net.Handler;

import com.project.zeng.bookstore.entities.Result;
import com.project.zeng.bookstore.entities.User;

/**
 * Created by zeng on 2017/3/8.
 * 用户信息网络响应的处理Handler
 */

public class UserHandler implements RespHandler<User, String> {

    /**
     * 用GSON解析出User（User包括Token）
     * @param data
     * @return
     */
    @Override
    public User parse(String data) {
        User user = gson.fromJson(data, User.class);
//        Log.e("UserHandler", user.getPasswordOrToken());
        return user;
    }

    /**
     * 用GSON解析出结果
     * @param data
     * @return
     */
    public static Result getResult(String data){
        return gson.fromJson(data, Result.class);
    }
}
