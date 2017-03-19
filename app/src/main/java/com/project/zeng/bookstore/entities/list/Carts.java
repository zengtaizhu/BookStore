package com.project.zeng.bookstore.entities.list;

import com.project.zeng.bookstore.entities.Cart;

import java.util.List;

/**
 * Created by zeng on 2017/3/17.
 * 购物车列表的实体
 */

public class Carts {

    private int count;//购物车的数目
    private List<Cart> carts;//购物车列表

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Cart> getCarts() {
        return carts;
    }

    public void setCarts(List<Cart> carts) {
        this.carts = carts;
    }
}
