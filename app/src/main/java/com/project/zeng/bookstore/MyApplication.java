package com.project.zeng.bookstore;

import android.app.Application;

import com.example.zeng.bookstore.R;
import com.project.zeng.bookstore.db.helper.DatabaseMgr;
import com.project.zeng.bookstore.net.mgr.RequestQueueMgr;

import cn.smssdk.SMSSDK;


/**
 * Created by zeng on 2017/2/22.
 * 初始化程序
 */

public class MyApplication extends Application {

    //全局变量
    private String token;//token令牌
    private String url;//网络地址

    @Override
    public void onCreate() {
        super.onCreate();
        setUrl("http://192.168.191.1:5000/api/v1.0/");
        setToken("");
        //初始化短信验证
        SMSSDK.initSDK(this, getString(R.string.APPKEY), getString(R.string.APPSECRET));
        //初始化数据库
        DatabaseMgr.init(this);
        //初始化网络请求
        RequestQueueMgr.init(this);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 退出程序时调用
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        DatabaseMgr.closeDatabase();
        RequestQueueMgr.getRequestQueue().stop();
    }
}
