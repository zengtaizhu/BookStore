package com.project.zeng.bookstore.net.Handler;

import android.util.Log;

import com.project.zeng.bookstore.entities.User;

/**
 * Created by zeng on 2017/3/8.
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
}
