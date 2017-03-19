package com.project.zeng.bookstore.entities;

import java.util.List;

/**
 * Created by zeng on 2017/2/25.
 * 购物车的实体
 */

public class Cart {

    /**
     * Cart的域
     */
    private String id;
    private List<Product> products;//商品列表
    private String totalprice;
    private String seller;//卖家ID
    public boolean isSelectAll = false;//购物车是否被选中

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }
}
