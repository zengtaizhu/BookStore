package com.project.zeng.bookstore.net;


import com.project.zeng.bookstore.entities.Cart;
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
    public void fetchCarts(String token, DataListener<List<Cart>> listener);
}
