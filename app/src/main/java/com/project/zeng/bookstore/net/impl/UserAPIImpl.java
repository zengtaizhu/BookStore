package com.project.zeng.bookstore.net.impl;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.project.zeng.bookstore.entities.Result;
import com.project.zeng.bookstore.entities.User;
import com.project.zeng.bookstore.listeners.DataListener;
import com.project.zeng.bookstore.net.Handler.UserHandler;
import com.project.zeng.bookstore.net.UserAPI;
import com.project.zeng.bookstore.utils.ImageToString;

import java.io.ByteArrayOutputStream;
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
     * 查看该用户是否存在
     * @param phone
     * @param listener
     */
    public void isUserExist(String phone, final DataListener<Result> listener){
        String realUrl = url + "users/phone/" + phone;
        StringRequest request = new StringRequest(realUrl, new Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(null != listener){
                    Log.e("UserAPIImpl", "response=" + response);
                    listener.onComplete(UserHandler.getResult(response));
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
                    Result result = UserHandler.getResult(response);//查看返回结果
                    if(result.getResult().contains("success")){
//                        Log.e("UserAPIImpl", "message=" + result.getMessage());
                        listener.onComplete(mRespHandler.parse(result.getMessage()));
                    }else{
                        listener.onComplete(null);
                    }
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
    public void modifyPassword(final User user, final DataListener<Result> listener){
        String realUrl = url + "users/modifyPassword/";
        StringRequest request = new StringRequest(Request.Method.POST, realUrl,
                new Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(null != listener){
                            listener.onComplete(UserHandler.getResult(response));
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
    public void registerUser(final User user, final DataListener<Result> listener) {
        String realUrl = url + "users/register/";
        StringRequest request = new StringRequest(Request.Method.POST, realUrl,
                new Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(null != listener){
                            listener.onComplete(UserHandler.getResult(response));
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

    /**
     * 通过令牌，更新用户头像
     * @param token
     * @param userImg
     * @param listener
     */
    @Override
    public void modifyUserImg(final String token, final Bitmap userImg, final DataListener<Result> listener) {
        String realUrl = url + "users/modifyUserImg/";
        StringRequest request = new StringRequest(Request.Method.POST, realUrl, new Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(null != response){
//                    Log.e("UserAPIImpl", "response=" + response);
                    listener.onComplete(UserHandler.getResult(response));
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
                params.put("token", token);
                params.put("image", ImageToString.change(userImg));
                return params;
            }
        };
        //将网络请求添加到网络请求队列
        performRequest(request);
    }

    /**
     * 通过令牌，修改用户名
     * @param token
     * @param listener
     */
    @Override
    public void modifyUserName(final String token, final String newUserName, final DataListener<Result> listener) {
        String realUrl = url + "modify/userName/";
        StringRequest request = new StringRequest(Request.Method.POST, realUrl, new Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(null != response){
//                    Log.e("UserAPIImpl", "response=" + response);
                    listener.onComplete(UserHandler.getResult(response));
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
                params.put("token", token);
                params.put("userName", newUserName);
                return params;
            }
        };
        //将请求添加到网络请求队列中
        performRequest(request);
    }

    /**
     * 通过令牌，修改用户性别
     * @param token
     * @param newSex
     * @param listener
     */
    @Override
    public void modifyUserSex(final String token, final String newSex, final DataListener<Result> listener) {
        String realUrl = url + "modify/userSex/";
        StringRequest request = new StringRequest(Request.Method.POST, realUrl, new Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(null != response){
//                    Log.e("UserAPIImpl", "response=" + response);
                    listener.onComplete(UserHandler.getResult(response));
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
                params.put("token", token);
                params.put("sex", newSex);
                return params;
            }
        };
        //将请求添加到网络请求队列中
        performRequest(request);
    }

    /**
     * 通过令牌，修改用户专业
     * @param token
     * @param newMajor
     * @param listener
     */
    @Override
    public void modifyUserMajor(final String token, final String newMajor, final DataListener<Result> listener) {
        String realUrl = url + "modify/userMajor/";
        StringRequest request = new StringRequest(Request.Method.POST, realUrl, new Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(null != response){
//                    Log.e("UserAPIImpl", "response=" + response);
                    listener.onComplete(UserHandler.getResult(response));
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
                params.put("token", token);
                params.put("major", newMajor);
                return params;
            }
        };
        //将请求添加到网络请求队列中
        performRequest(request);
    }

    /**
     * 通过令牌，修改用户年级
     * @param token
     * @param newGrade
     * @param listener
     */
    @Override
    public void modifyUserGrade(final String token, final String newGrade, final DataListener<Result> listener) {
        String realUrl = url + "modify/userGrade/";
        StringRequest request = new StringRequest(Request.Method.POST, realUrl, new Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(null != response){
//                    Log.e("UserAPIImpl", "response=" + response);
                    listener.onComplete(UserHandler.getResult(response));
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
                params.put("token", token);
                params.put("grade", newGrade);
                return params;
            }
        };
        //将请求添加到网络请求队列中
        performRequest(request);
    }

    /**
     * 获得专业列表
     * @param listener
     */
    @Override
    public void fetchMajors(final DataListener<String[]> listener) {
        final String realUrl = url + "user/majors/";
        StringRequest request = new StringRequest(realUrl, new Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(null != response){
//                    Log.e("UserAPIImpl", "response=" + response);
                    String majors = UserHandler.getResult(response).getMessage();
                    listener.onComplete(majors.split(","));
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
     * 获得年级列表
     * @param listener
     */
    @Override
    public void fetchGrades(final DataListener<String[]> listener) {
        final String realUrl = url + "user/grades/";
        StringRequest request = new StringRequest(realUrl, new Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(null != response){
//                    Log.e("UserAPIImpl", "response=" + response);
                    String grades = UserHandler.getResult(response).getMessage();
                    listener.onComplete(grades.split(","));
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
     * 通过令牌，修改用户收货地址
     * @param token
     * @param newLocation
     * @param listener
     */
    @Override
    public void modifyUserLocation(final String token, final String newLocation, final DataListener<Result> listener) {
        String realUrl = url + "modify/userLocation/";
        StringRequest request = new StringRequest(Request.Method.POST, realUrl, new Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(null != response){
                    Log.e("UserAPIImpl", "response=" + response);
                    listener.onComplete(UserHandler.getResult(response));
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
                params.put("token", token);
                params.put("location", newLocation);
                return params;
            }
        };
        //将请求添加到网络请求队列中
        performRequest(request);
    }
}
