package com.project.zeng.bookstore.db.engine;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Process;

import com.project.zeng.bookstore.db.cmd.Command;
import com.project.zeng.bookstore.listeners.DataListener;

/**
 * Created by zeng on 2017/2/24.
 * 数据库操作执行引擎
 */

public class DbExecutor {
    //HandlerThread内部封装了自己的Handler和Thread，有单独的Looper和消息队列
    private static final HandlerThread HT = new HandlerThread(
            DbExecutor.class.getName(),
            Process.THREAD_PRIORITY_BACKGROUND);
    static {
        //默认加载开启HandlerThread队列
        HT.start();
    }

    //异步线程的Handler
    final static Handler sAsyncHandler = new Handler(HT.getLooper());

    //主线程消息队列的Handler
    final static Handler mUIHandler = new Handler(Looper.getMainLooper());

    //可供调用的本身
    static DbExecutor mDispatcher =  new DbExecutor();

    private DbExecutor(){
    }

    public static DbExecutor getDbExecutor(){
        return mDispatcher;
    }

    public Handler getUIHandler(){
        return mUIHandler;
    }

    public void submit(Runnable runnable){
        sAsyncHandler.post(runnable);
    }

    public <T> void execute(final Command<T> command){
        sAsyncHandler.post(new Runnable() {
            @Override
            public void run() {
                T result = command.execute();
                if(command.dataListener != null){
                    //执行数据库操作命令，将结果传递给UI线程
                    postResult(result, command.dataListener);
                }
            }
        });
    }

    /**
     * 将数据传递给UI线程
     * @param result
     * @param listener
     * @param <T>
     */
    private <T> void postResult(final T result, final DataListener<T> listener){
        mUIHandler.post(new Runnable() {
            @Override
            public void run() {
                listener.onComplete(result);
            }
        });
    }
}
