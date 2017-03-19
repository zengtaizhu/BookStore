package com.project.zeng.bookstore.net;

import com.project.zeng.bookstore.entities.User;
import com.project.zeng.bookstore.listeners.DataListener;

/**
 * Created by zeng on 2017/3/8.
 * 登录令牌及用户信息的网络请求的接口
 */

public interface UserAPI {

    /**
     * 通过账号密码，获得用户信息（包括登录令牌，用令牌替代密码）
     * @param user
     * @param listener
     */
    public void fetchUserAndToken(User user, DataListener<User> listener);

    /**
     * 通过用户的ID，获得用户信息
     * @param id
     * @param listener
     */
    public void fetchUserById(String id, DataListener<User> listener);

    /**
     * 修改密码
     * @param user
     * @param listener
     */
    public void modifyPassword(User user, DataListener<String> listener);

    /**
     * 注册新用户
     * @param user
     * @param listener
     */
    public void registerUser(User user, DataListener<String> listener);

    /**
     * 通过用户，获取用户信息（用于重设密码）
     * @param user
     * @param listener
     */
    public void fetchUserById(User user, DataListener<User> listener);
}
