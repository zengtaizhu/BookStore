package com.project.zeng.bookstore.entities;

import java.io.Serializable;

/**
 * Created by zeng on 2017/2/23.
 */

public class Product implements Serializable{

    /**
     * Product的域
     */
    private String id;
    private String category_id;
    private String seller_id;
    private String pictureUrl;
    private String title;
    private String author;
    private String press;
    private double price;
    private int count;
    private String describe;
    private String suitableGrade;//适合的年级
    public boolean isSelected = false;//购物车是否被选中

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryId() {
        return category_id;
    }

    public void setCategoryId(String category_id) {
        this.category_id = category_id;
    }

    public String getSellerId() {
        return seller_id;
    }

    public void setSellerId(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getSuitableGrade() {
        return suitableGrade;
    }

    public void setSuitableGrade(String suitableGrade) {
        this.suitableGrade = suitableGrade;
    }
}
