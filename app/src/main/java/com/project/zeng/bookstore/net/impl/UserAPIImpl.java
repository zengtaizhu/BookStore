package com.project.zeng.bookstore.net.impl;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.project.zeng.bookstore.MyApplication;
import com.project.zeng.bookstore.entities.User;
import com.project.zeng.bookstore.listeners.DataListener;
import com.project.zeng.bookstore.net.Handler.UserHandler;
import com.project.zeng.bookstore.net.UserAPI;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zeng on 2017/3/8.
 * 用户信息网络请求的实现类
 */

public class UserAPIImpl extends AbsNetwork<User, String> implements UserAPI{

    private String url = "http://192.168.191.1:5000/api/v1.0/";

    public UserAPIImpl() {
        mRespHandler = new UserHandler();
    }

    /**
     * 通过账号密码，获得用户信息（包括登录令牌，登录令牌替换本地数据库的密码）
     * @param user
     * @param listener
     */
    @Override
    public void fetchUserAndToken(final User user, final DataListener<User> listener) {
        //格式为：http://127.0.0.1:5000/api/v1.0/token/ POST方法
        String realUrl = url + "token/";
        StringRequest request = new StringRequest(Request.Method.POST, realUrl,
                new Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Log.e("UserAPIImpl", "user=" + response);
                if(null != listener){
                    listener.onComplete(mRespHandler.parse(response));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("UserAPIImpl", error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //设置POST的请求参数
                Map<String, String> params = new HashMap<>();
                params.put("id", user.getId());
                params.put("password", user.getPasswordOrToken());
                return params;
            }
        };
        //将请求添加到网络请求队列中
        performRequest(request);
    }

    /**
     * 通过用户的ID，获得用户信息
     * @param id
     * @param listener
     */
    @Override
    public void fetchUserById(final String id, final DataListener<User> listener) {
        String realUrl = url + "users/" + id;
        StringRequest request = new StringRequest(realUrl, new Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Log.e("UserAPIImpl", "result=" + response);
                if (null != listener) {
                    listener.onComplete(mRespHandler.parse(response));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("UserAPIImpl", error.getMessage());
            }
        });
        //将请求添加到网络请求队列中
        performRequest(request);
    }

    /**
     * 修改密码
     * @param user
     * @param listener
     */
    @Override
    public void modifyPassword(final User user, final DataListener<String> listener){
        String realUrl = url + "users/modifyPassword/";
        StringRequest request = new StringRequest(Request.Method.POST, realUrl,
                new Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(null != listener){
                            listener.onComplete(response);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("UserAPIImpl", error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", user.getId());
                params.put("password", user.getPasswordOrToken());
                return params;
            }
        };
        //将请求添加到网络请求队列中
        performRequest(request);
    }

    /**
     * 注册新用户
     * @param user
     * @param listener
     */
    @Override
    public void registerUser(final User user, final DataListener<String> listener) {
        String realUrl = url + "users/register/";
        StringRequest request = new StringRequest(Request.Method.POST, realUrl,
                new Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(null != listener){
                            listener.onComplete(response);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("UserAPIImpl", error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", user.getId());
                params.put("password", user.getPasswordOrToken());
                return params;
            }
        };
        //将请求添加到网络请求队列中
        performRequest(request);
    }

    /**
     * 通过无令牌的用户信息，来获取包括令牌的用户（用于重设密码）
     * @param user
     * @param listener
     */
    @Override
    public void fetchUserById(final User user, final DataListener<User> listener) {
        String realUrl = url + "users/phone/";
        StringRequest request = new StringRequest(Request.Method.POST, realUrl,
                new Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.e("UserAPIImpl", "result=" + response);
                        if(null != listener){
                            listener.onComplete(mRespHandler.parse(response));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("UserAPIImpl", error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", user.getId());
                return params;
            }
        };
        //将请求添加到网络请求队列中
        performRequest(request);
    }
}
