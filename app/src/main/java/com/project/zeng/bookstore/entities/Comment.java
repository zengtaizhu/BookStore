package com.project.zeng.bookstore.entities;

import java.sql.Date;

/**
 * Created by zeng on 2017/3/15.
 * 商品评论的实体
 */

public class Comment {

    /**
     * Comment的域
     */
    private String id;
    private Date comment_time;
    private String grade;
    private String comment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getComment_time() {
        return comment_time;
    }

    public void setComment_time(Date comment_time) {
        this.comment_time = comment_time;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
