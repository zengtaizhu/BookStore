package com.project.zeng.bookstore.entities.list;

import com.project.zeng.bookstore.entities.Product;

import java.util.List;

/**
 * Created by zeng on 2017/3/2.
 * 商品的列表
 */

public class Products {

    private int count;//商品的总数量
    private List<Product> products;//商品的列表
    private String prev;//上一页链接
    private String next;//下一页链接

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getPrev() {
        return prev;
    }

    public void setPrev(String prev) {
        this.prev = prev;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }
}
