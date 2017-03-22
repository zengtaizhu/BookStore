package com.project.zeng.bookstore.entities.list;

import com.project.zeng.bookstore.entities.Order;

import java.util.List;

/**
 * Created by zeng on 2017/3/21.
 * 订单的数组
 */

public class Orders {

    private int count;//订单的数量
    private List<Order> orders;//订单列表

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
