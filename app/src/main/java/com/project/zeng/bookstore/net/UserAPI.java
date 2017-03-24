package com.project.zeng.bookstore.net;

import android.graphics.Bitmap;

import com.project.zeng.bookstore.entities.Result;
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

    /**
     * 通过令牌，更新用户头像
     * @param token
     * @param userImg
     * @param listener
     */
    public void modifyUserImg(String token, Bitmap userImg, DataListener<Result> listener);

    /**
     * 通过令牌，修改用户名
     * @param token
     * @param listener
     */
    public void modifyUserName(String token, String newUserName, DataListener<Result> listener);

    /**
     * 通过令牌，修改用户性别
     * @param token
     * @param newSex
     * @param listener
     */
    public void modifyUserSex(String token, String newSex, DataListener<Result> listener);

    /**
     * 通过令牌，修改用户专业
     * @param token
     * @param newMajor
     * @param listener
     */
    public void modifyUserMajor(String token, String newMajor, DataListener<Result> listener);

    /**
     * 通过令牌，修改用户年级
     * @param token
     * @param Grade
     * @param listener
     */
    public void modifyUserGrade(String token, String Grade, DataListener<Result> listener);

    /**
     * 获得专业列表
     * @param listener
     */
    public void fetchMajors(DataListener<String[]> listener);

    /**
     * 获得年级列表
     * @param listener
     */
    public void fetchGrades(DataListener<String[]> listener);
}
