package com.project.zeng.bookstore;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

/**
 * Created by zeng on 2017/3/29.
 * 网络状态广播接收器
 */

public class NetState extends BroadcastReceiver{

    private Context mContext;
    public static boolean isAvailable = true;//网络是否能用

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                //提醒网络无连接
                case 0x210:
                    Toast.makeText(mContext, "您的网络在偷懒，请重试", Toast.LENGTH_SHORT).show();
                    sendEmptyMessageDelayed(0x210, 30 * 1000);//每分钟提醒一次
                    break;
                case 0x211:
                    mHandler.removeMessages(0x210);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo gprsInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(!gprsInfo.isConnected() && !wifiInfo.isConnected()){
            isAvailable = false;
            mHandler.sendEmptyMessageDelayed(0x210, 30 * 1000);//每分钟提醒一次
        }else{
            isAvailable = true;
            mHandler.sendEmptyMessage(0x211);
        }
    }
}
