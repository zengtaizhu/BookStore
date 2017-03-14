package com.project.zeng.bookstore.entities;

/**
 * Created by zeng on 2017/2/23.
 */

public class Category {

    /**
     * Category的域
     */
    private String id;
    private String name;
    private String suitableGrade;
    private String pictureUrl;
    private String describe;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getSuitableGrade() {
        return suitableGrade;
    }

    public void setSuitableGrade(String suitableGrade) {
        this.suitableGrade = suitableGrade;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    @Override
    public String toString() {
        return "Category [id=" + this.id + ",name=" + this.name +
                ",suitableGrade=" + this.suitableGrade + ",describe=" +
                this.describe + ",pictureUrl=" + this.pictureUrl + "]";
    }
}
