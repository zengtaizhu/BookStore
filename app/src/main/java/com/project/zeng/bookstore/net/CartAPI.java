package com.project.zeng.bookstore.net;


import com.project.zeng.bookstore.entities.Cart;
import com.project.zeng.bookstore.entities.Result;
import com.project.zeng.bookstore.listeners.DataListener;

import java.util.List;

/**
 * Created by zeng on 2017/3/17.
 * 购物车网络请求的接口
 */

public interface CartAPI{

    /**
     * 通过令牌，获取购物车列表
     * @param token 令牌
     * @param listener
     */
    void fetchCarts(String token, DataListener<List<Cart>> listener);

    /**
     * 通过令牌，将商品ID为proId的商品添加到购物车中
     * @param token
     * @param proId
     * @param listener
     */
    void addProToCart(String token, String proId, int count, DataListener<Result> listener);

    /**
     * 通过令牌，删除购物车
     * @param token
     * @param id
     * @param listener
     */
    void deleteCart(String token, String id, DataListener<Result> listener);

    /**
     * 通过令牌，删除购物车上的商品
     * @param token
     * @param proId
     * @param cartId
     * @param listener
     */
    void deleteProFromCart(String token, String proId, String cartId, DataListener<Result> listener);
}
