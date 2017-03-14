package com.project.zeng.bookstore.listeners;

/**
 * Created by zeng on 2017/2/24.
 * 通用数据Listener
 * @param <T>
 */
public interface DataListener<T> {
    public void onComplete(T result);
}
