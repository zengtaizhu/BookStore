package com.project.zeng.bookstore.db.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by zeng on 2017/2/24.
 * 数据库实例管理类，引用技术管理数据库实例，当计时为0时关闭数据库
 */

public class DatabaseMgr {
    private static SQLiteDatabase sDatabase;
    //自动增长的整型数据，代表目前引用数据库的次数
    private static AtomicInteger sDbRef = new AtomicInteger(0);
    static DatabaseHelper sHelper;

    /**
     * 初始化数据库，使用锁防止多线程同时初始化
     * @param context
     */
    public static void init(Context context){
        if(sDatabase == null && context != null){
            synchronized (DatabaseMgr.class){
                sHelper = new DatabaseHelper(context.getApplicationContext());
                sDatabase = sHelper.getWritableDatabase();
            }
        }
    }

    /**
     * 若无引用，则关闭数据库
     */
    public static void releaseDatabase(){
        if(sDbRef.decrementAndGet() == 0){
            closeDatabase();
        }
    }

    /**
     * 关闭数据库，回收数据库内存
     */
    public static void closeDatabase(){
        if(sDatabase != null && sDatabase.isOpen()){
            sDatabase.close();
            sDatabase = null;
        }
    }

    /**
     * 获得数据库实例，记录引用次数
     * @return
     */
    public static SQLiteDatabase getDatabase(){
        if(!sDatabase.isOpen()){
            sDatabase = sHelper.getWritableDatabase();
        }
        sDbRef.incrementAndGet();
        return sDatabase;
    }

    /**
     * 开始数据库事务
     */
    public static void beginTransaction(){
        if(sDatabase != null){
            sDatabase.beginTransaction();
        }
    }

    /**
     * 设置当前数据库的事务的状态为成功，在事务开始和结束之间调用
     */
    public static void setTransactionSuccess(){
        if(sDatabase != null){
            sDatabase.setTransactionSuccessful();
        }
    }

    /**
     * 结束数据库事务
     */
    public static void endTransaction(){
        if(sDatabase != null){
            sDatabase.endTransaction();
        }
    }
}
