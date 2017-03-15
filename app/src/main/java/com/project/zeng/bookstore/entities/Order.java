package com.project.zeng.bookstore.entities;

import java.sql.Date;
import java.util.List;

/**
 * Created by zeng on 2017/2/25.
 */

public class Order {

    /**
     * Order的域
     */
    private String id;
    private List<Product> products;//products包含多个商品,格式为：ID|count,ID2|count2-----待修改，将products替代为List<Product>
    private Date submit_time;
    private Date deliver_time;
    private String state;
    private String phone;
    private String address;
    private double totalprice;
    private String comment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Date getSubmit_time() {
        return submit_time;
    }

    public void setSubmit_time(Date submit_time) {
        this.submit_time = submit_time;
    }

    public Date getDeliver_time() {
        return deliver_time;
    }

    public void setDeliver_time(Date deliver_time) {
        this.deliver_time = deliver_time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(double totalprice) {
        this.totalprice = totalprice;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
