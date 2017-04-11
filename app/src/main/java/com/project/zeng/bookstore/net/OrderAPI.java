package com.project.zeng.bookstore.net;

import com.project.zeng.bookstore.entities.Order;
import com.project.zeng.bookstore.entities.Result;
import com.project.zeng.bookstore.listeners.DataListener;

import java.util.List;

/**
 * Created by zeng on 2017/3/22.
 * 订单网络请求的接口
 */

public interface OrderAPI{
    String[] ORDER_STATE = new String[]{"DELIVERING", "RECEIVEING", "COMMENTING", "DONE", "RETURNING"};//订单状态常量

    /**
     * 通过Token令牌，获取所有订单
     * @param token
     * @param listener
     */
    void fetchOrders(String token, DataListener<List<Order>> listener);

    /**
     * 通过Token令牌，获取符合state状态的订单（对于买家）
     * @param token
     * @param state
     * @param listener
     */
    void fetchOrdersByState(String token, int state, DataListener<List<Order>> listener);

    /**
     * 通过Token令牌，获取符合state状态的订单（对于卖家）
     * @param token
     * @param state
     * @param listener
     */
    void fetchOwnOrdersByState(String token, int state, DataListener<List<Order>> listener);

    /**
     * 提交订单
     * @param token
     * @param newOrder
     * @param listener
     */
    void submitOrder(String token, Order newOrder, String products, DataListener<Result> listener);

    /**
     * 通过令牌，修改订单的状态
     * @param token
     * @param orderId
     * @param state
     * @param listener
     */
    void modifyOrderState(String token, String orderId, int state, DataListener<Result> listener);

    /**
     * 通过令牌，修改订单
     * @param token
     * @param order
     * @param state
     * @param listener
     */
    void modifyOrder(String token, Order order, int state, DataListener<Result> listener);

    /**
     * 通过令牌，对订单进行评价
     * @param token
     * @param orderId
     * @param state
     * @param comment
     * @param listener
     */
    void commentOrder(String token, String orderId, int state, String comment, DataListener<Result> listener);

    /**
     * 通过令牌，删除订单
     * @param token
     * @param orderId
     * @param listener
     */
    void deleteOrder(String token, String orderId, DataListener<Result> listener);
}
