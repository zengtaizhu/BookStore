package com.project.zeng.bookstore.entities.list;

import com.project.zeng.bookstore.entities.Comment;

import java.util.List;

/**
 * Created by zeng on 2017/3/16.
 * 商品评论的数组
 */

public class Comments {

    private int count;//商品评论的数目
    private List<Comment> comments;//商品评论列表

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
