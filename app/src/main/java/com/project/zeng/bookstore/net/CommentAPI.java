package com.project.zeng.bookstore.net;

import com.project.zeng.bookstore.entities.Comment;
import com.project.zeng.bookstore.listeners.DataListener;

import java.util.List;

/**
 * Created by zeng on 2017/3/16.
 * 获得商品评论的接口
 */

public interface CommentAPI {

    /**
     * 通过商品ID，获得评论
     * @param proId
     * @param listener
     */
    public void fetchComment(String proId, DataListener<List<Comment>> listener);
}
