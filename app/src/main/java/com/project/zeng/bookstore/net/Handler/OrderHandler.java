package com.project.zeng.bookstore.net.Handler;

import com.project.zeng.bookstore.entities.Order;
import com.project.zeng.bookstore.entities.list.Orders;

import java.util.List;

/**
 * Created by zeng on 2017/3/22.
 * 订单网络响应的处理Handler
 */

public class OrderHandler implements RespHandler<List<Order>,String> {

    /**
     * 解析数据，获得订单列表
     * @param data
     * @return
     */
    @Override
    public List<Order> parse(String data) {
        List<Order> orders = gson.fromJson(data, Orders.class).getOrders();
        return orders;
    }
}
