package com.project.zeng.bookstore.listeners;

/**
 * Created by zeng on 2017/2/23.
 * item点击事件的接口
 */

public interface OnItemClickListener<T> {
    public void onClick(T item);
}
