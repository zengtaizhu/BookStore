package com.project.zeng.bookstore.entities;

import java.util.List;

/**
 * Created by zeng on 2017/2/25.
 */

public class Cart {

    /**
     * Cart的域
     */
    private String id;
    private List<Product> products;//---------------待修改，将products替代为List<Product>
    private String totalprice;

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

    public String getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }
}
