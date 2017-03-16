package com.project.zeng.bookstore.net.Handler;

import com.project.zeng.bookstore.entities.Comment;
import com.project.zeng.bookstore.entities.list.Comments;

import java.util.List;

/**
 * Created by zeng on 2017/3/16.
 * 商品评论的Handler
 */

public class CommentHandler implements RespHandler<List<Comment>, String>{

    /**
     * 使用Gson解析出商品评论列表
     * @param data
     * @return
     */
    @Override
    public List<Comment> parse(String data) {
        List<Comment> comments = gson.fromJson(data, Comments.class).getComments();
        return comments;
    }
}
