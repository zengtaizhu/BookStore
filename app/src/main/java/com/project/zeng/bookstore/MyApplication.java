package com.project.zeng.bookstore;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.example.zeng.bookstore.R;
import com.project.zeng.bookstore.db.helper.DatabaseMgr;
import com.project.zeng.bookstore.entities.User;
import com.project.zeng.bookstore.net.mgr.RequestQueueMgr;

import cn.smssdk.SMSSDK;


/**
 * Created by zeng on 2017/2/22.
 * 初始化程序
 */

public class MyApplication extends Application {

    //全局变量
    private String token;//token令牌------------待修改为用用户代替
    private String url;//网络地址
    private User user;//用户
    private NetState receiver;

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
        //网络监听器
        initReceiver();
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * 开始获取令牌的service
     */
    public void startGetTokenService(){
        startService(new Intent(this, GetTokenService.class));
    }

    /**
     * 停止获取令牌的service
     */
    public void stopGetTokenService(){
        stopService(new Intent(this, GetTokenService.class));
    }

    public NetState getReceiver() {
        return receiver;
    }

    /**
     * 初始化网络状态监听器
     */
    public void initReceiver() {
        receiver = new NetState();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(receiver, filter);//注册监听器
        receiver.onReceive(this, null);
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
