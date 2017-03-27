package com.project.zeng.bookstore;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.project.zeng.bookstore.entities.User;
import com.project.zeng.bookstore.listeners.DataListener;
import com.project.zeng.bookstore.net.UserAPI;
import com.project.zeng.bookstore.net.impl.UserAPIImpl;

/**
 * Created by zeng on 2017/3/25.
 * 定时获取Token的Service
 */

public class GetTokenService extends Service{

    //全局变量
    private MyApplication app;
    //用户信息的网络请求
    UserAPI mUserAPI = new UserAPIImpl();

    int anHour = 60 * 60 * 1000;//一个小时的秒数
    int timer = anHour / 6 * 5;//一小时的六分之五，即50分钟

    @Override
    public void onCreate() {
        super.onCreate();
        app = (MyApplication) getApplication();
        Log.e("GetTokenService", "onCreate()");
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x999:
                    mUserAPI.fetchUserById(app.getUser(), new DataListener<User>() {
                        @Override
                        public void onComplete(User result) {
                            if(null != result){
                                app.setUser(result);
                                app.setToken(result.getPasswordOrToken());//更新Token令牌
                            }
                        }
                    });
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mHandler.sendEmptyMessageDelayed(0x999, timer);//每50分钟获取一次Token
        Log.e("GetTokenService", "onStartCommand()");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        mHandler.removeMessages(0x999);//停止获取token
        Log.e("GetTokenService", "onDestroy()");
        super.onDestroy();
    }
}
