package com.project.zeng.bookstore.entities;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zeng on 2017/3/15.
 * 商品评论的实体
 */

public class Comment {

    /**
     * Comment的域
     */
    private String id;
    private String buyer_id;
    private String buyer_img;
    private String comment_time;
    private String comment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBuyer_id() {
        return buyer_id;
    }

    public void setBuyer_id(String buyer_id) {
        this.buyer_id = buyer_id;
    }

    public String getBuyer_img() {
        return buyer_img;
    }

    public void setBuyer_img(String buyer_img) {
        this.buyer_img = buyer_img;
    }

    public String getComment_time() {
        return comment_time;
    }

    public void setComment_time(String comment_time) {
        this.comment_time = comment_time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
